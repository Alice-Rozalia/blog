package com.kuro.service.impl;

import com.aliyun.oss.ServiceException;
import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.auth.JWTToken;
import com.kuro.auth.jwt.JwtProperties;
import com.kuro.auth.jwt.JwtUtils;
import com.kuro.common.entity.*;
import com.kuro.common.enums.UserTypeEnum;
import com.kuro.mapper.LoginLogMapper;
import com.kuro.mapper.MenuMapper;
import com.kuro.mapper.UserMapper;
import com.kuro.mapper.UserRoleMapper;
import com.kuro.model.entity.*;
import com.kuro.model.vo.MenuVo;
import com.kuro.model.vo.UserInfoVo;
import com.kuro.service.UserService;
import com.kuro.utils.*;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private JwtProperties prop;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private SmsListener smsListener;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String key_prefix = "user: verify";

    /**
     * 分页查询用户列表，不传参数默认查询所有
     * @param key
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<User> findUserList(String key, Integer page, Integer rows) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        // 添加搜索条件
        if (StringUtils.isNoneBlank(key)) {
            criteria.andLike("username", "%" + key + "%");
        }
        // 添加分页
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        List<User> users = this.userMapper.selectByExample(example);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return new PageResult<>(pageInfo.getTotal(), users);
    }

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    @Override
    public User findUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return this.userMapper.selectOne(user);
    }

    /**
     * 查询用户角色
     * @param id
     * @return
     */
    @Override
    public List<Role> findRolesById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_ACCOUNT_NOT_EXIST.getMessage());
        }
        return userMapper.findRolesById(id);
    }

    /**
     * 根据用户角色查询用户的菜单
     * @param roles
     * @return
     */
    @Override
    public List<Menu> findMenuByRoles(List<Role> roles) {
        Long[] roleIds = new Long[roles.size()];
        roles.forEach(role -> {
            roleIds[0] = role.getId();
        });
        return this.userMapper.findMenuByRoleId(roleIds[0]);
    }

    @Override
    public List<MenuVo> findMenus() {
        List<Menu> menus = null;
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode()) {
            Menu menu = new Menu();
            menu.setType(0);
            menus = this.menuMapper.select(menu);
        } else if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_FRIEND.getTypeCode()) {
            menus = activeUser.getMenus();
        }
        return MenuTreeBuilder.build(menus);
    }

    /**
     * 登录
     * @param email
     * @param password
     * @return
     */
    @Override
    @Transactional
    public Result login(String email, String password, HttpServletRequest request) {
        // 通过手机号查找该用户
        User one = this.findUserByPhone(email);
        if (one == null) {
            return Result.custom(ResultCode.USER_CREDENTIALS_ERROR);
        }
        // 验证密码
        String salt = one.getSalt();
        if (!StringUtils.equals(CodecUtils.md5Hex(password, salt), one.getPassword())) {
            return Result.custom(ResultCode.USER_CREDENTIALS_ERROR);
        }
        // 记录登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setNickname(one.getNickname());
        // 获取 ip 地址
        loginLog.setIp(IPUtil.getIpAddr(request));
        // 获取地理位置
        loginLog.setLocation(AddressUtil.getCityInfo(IPUtil.getIpAddr(request)));
        // loginLog.setLocation(IpToAddressUtil.getCityInfo(IPUtil.getIpAddr(request)));
        // 获取客户端操作系统
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取浏览器
        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();
        loginLog.setUserSystem(os.getName());
        loginLog.setUserBrowser(browser.getName());
        loginLog.setLoginTime(System.currentTimeMillis());

        // 生成 token 令牌
        UserInfo info = new UserInfo();
        info.setId(one.getId());
        info.setAvatar(one.getAvatar());
        info.setPhone(one.getPhone());
        info.setNickname(one.getNickname());
        try {
            String token = JwtUtils.generateToken(info, this.prop.getPrivateKey(), this.prop.getExpire());
            JWTToken jwtToken = new JWTToken(token);
            SecurityUtils.getSubject().login(jwtToken);
            this.loginLogMapper.insertSelective(loginLog);
            return Result.custom(ResultCode.LOGIN_SUCCESS).data("token", token);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message(e.getMessage());
        }
    }

    /**
     * 用户详情
     * @return
     */
    @Override
    public UserInfoVo info() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setAvatar(activeUser.getUser().getAvatar());
        userInfoVo.setNickname(activeUser.getUser().getNickname());
        userInfoVo.setUrl(activeUser.getUrls());
        List<String> roleNames = activeUser.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        userInfoVo.setRoles(roleNames);
        userInfoVo.setPerms(activeUser.getPermissions());
        userInfoVo.setIsAdmin(activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode() || activeUser.getUser().getType() == UserTypeEnum.SYSTEM_FRIEND.getTypeCode());
        return userInfoVo;
    }

    /**
     * 发送验证码，1为注册，2为改密
     * @param phone
     * @param type
     * @return
     * @throws ClientException
     */
    @Override
    public Result sendCode(String phone, Integer type) throws ClientException {
        if (StringUtils.isBlank(phone)) {
            return Result.custom(ResultCode.PARAM_IS_BLANK);
        }
        // 生成6位随机验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        if (type == 1) {
            // 注册
            smsListener.sendSms(msg, "SMS_200692839");
        } else {
            // 改密
            smsListener.sendSms(msg, "SMS_206563694");
        }

        // 把验证码保存到redis
        this.redisTemplate.opsForValue().set(key_prefix + phone, code, 5, TimeUnit.MINUTES);
        return Result.custom(ResultCode.PHONE_CODE_SEND);
    }

    @Override
    @Transactional
    public Result register(User user, String code) {
        // 判断该用户是否已经存在
        User phoneCheck = new User();
        phoneCheck.setPhone(user.getPhone());
        User checkByPhone = this.userMapper.selectOne(phoneCheck);
        if (checkByPhone != null) {
            return Result.custom(ResultCode.PHONE_ACCOUNT_ALREADY_EXIST);
        }
        // 校验用户名和手机号使用已经被注册
        if (!checkUser(user.getNickname(), 1)) {
            return Result.custom(ResultCode.NICKNAME_ACCOUNT_ALREADY_EXIST);
        }
        if (!checkUser(user.getPhone(), 2)) {
            return Result.custom(ResultCode.PHONE_ACCOUNT_ALREADY_EXIST);
        }
        // 查询 redis 中的验证码，并校验
        String redisCode = this.redisTemplate.opsForValue().get(key_prefix + user.getPhone());
        if (!StringUtils.equals(code, redisCode)) {
            return Result.custom(ResultCode.CODE_ERROR);
        }
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setStatus(true);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setType(UserTypeEnum.SYSTEM_USER.getTypeCode());
        user.setCreateTime(System.currentTimeMillis());
        user.setAvatar(getRandomImg());
        int result = this.userMapper.insertSelective(user);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        UserRole userRole = new UserRole();
        userRole.setRoleId(2L);
        userRole.setUserId(this.userMapper.selectOne(user).getId());
        this.userRoleMapper.insertSelective(userRole);
        // 删除验证码
        this.redisTemplate.delete(key_prefix + user.getPhone());
        return Result.custom(ResultCode.REGISTER_SUCCESS);
    }

    @Override
    public Result updateStatus(Long id, Boolean status) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_UPDATE_STATUS_NOT_EXIST.getMessage());
        }
        User carrier = new User();
        carrier.setId(id);
        carrier.setStatus(status);
        int result = this.userMapper.updateByPrimaryKeySelective(carrier);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    /**
     * 校验用户名或手机号是否可用
     * @param data 要校验的值
     * @param type （1,2）  1表示校验用户名，2表示校验手机号
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if (type == 1) {
            record.setNickname(data);
        } else if (type == 2) {
            record.setPhone(data);
        } else {
            // 参数不合法
            return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

    @Override
    public Result changePassword(User user, String code) {
        // 判断该用户是否已经存在
        User phoneCheck = new User();
        phoneCheck.setPhone(user.getPhone());
        User checkByPhone = this.userMapper.selectOne(phoneCheck);
        if (checkByPhone == null) {
            return Result.custom(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        // 查询 redis 中的验证码，并校验
        String redisCode = this.redisTemplate.opsForValue().get(key_prefix + user.getPhone());
        if (!StringUtils.equals(code, redisCode)) {
            return Result.custom(ResultCode.CODE_ERROR);
        }
        String salt = CodecUtils.generateSalt();
        checkByPhone.setSalt(salt);
        checkByPhone.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        checkByPhone.setModifiedTime(System.currentTimeMillis());
        int result = this.userMapper.updateByPrimaryKeySelective(checkByPhone);
        if (result == 0) {
            return Result.custom(ResultCode.PASSWORD_CHANGE_ERROR);
        }
        // 删除验证码
        this.redisTemplate.delete(key_prefix + user.getPhone());
        return Result.custom(ResultCode.PASSWORD_CHANGE_SUCCESS);
    }

    /**
     * 放在CDN上的随机头像
     */
    private static final String [] headImg = {
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/12.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/11.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/13.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/14.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/15.jpeg"
    };

    private String getRandomImg(){
        int size =  headImg.length;
        Random random = new Random();
        int index = random.nextInt(size);
        return headImg[index];
    }
}
