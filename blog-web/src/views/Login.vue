<template>
  <div class="limiting-surface">
    <div class="login-container">
      <el-card>
        <el-divider>
          <h1>登录</h1>
        </el-divider>

        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form" label-position="left"
          autocomplete="on">
          <el-form-item prop="phone">
            <el-input prefix-icon="el-icon-mobile-phone" ref="phone" type="text" v-model="loginForm.phone" name="phone"
              autocomplete="on" placeholder="手机号" tabindex="1" />
          </el-form-item>

          <el-tooltip v-model="capsTooltip" content="大写锁定已打开" placement="right" manual>
            <el-form-item prop="password">
              <el-input prefix-icon="el-icon-lock" show-password ref="password" v-model="loginForm.password"
                type="password" placeholder="密码" name="password" tabindex="2" autocomplete="on"
                @keyup.native="checkCapslock" @blur="capsTooltip = false" @keyup.enter.native="login" />
            </el-form-item>
          </el-tooltip>

          <el-form-item style="width:100%">
            <el-button :loading="loading" type="primary" style="width:100%; border: none" v-on:click="login">登录
            </el-button>
          </el-form-item>
        </el-form>

        <div class="signin">
          <router-link to="/register">注册</router-link>
          <a href="javascript:;" @click="dialogFormVisible = true">忘记密码</a>
        </div>

        <el-divider>其他登录方式</el-divider>

        <div class="other-login">
          <el-popover placement="bottom" width="80" trigger="hover" content="暂未开发">
            <img slot="reference" class="svgImg" src="@/assets/QQ.svg" />
          </el-popover>
        </div>
      </el-card>

      <!-- 验证码 -->
      <Vcode :show="isShow" @success="success" @close="close" :canvasWidth="500" :canvasHeight="350" />
    </div>

    <!-- 忘记密码 -->
    <el-dialog title="忘记密码" :visible.sync="dialogFormVisible" width="33%">
      <el-form label-width="70px" ref="forgetRef" :rules="forgetRules" :model="forgetForm">
        <el-form-item label="手机号" prop="phone">
          <el-input prefix-icon="el-icon-mobile-phone" placeholder="手机号" v-model="forgetForm.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input prefix-icon="el-icon-key" style="width: 40%;margin-right: 20px" type="text" v-model="forgetForm.code" name="code" autocomplete="on" placeholder="短信验证码" tabindex="4" />
          <el-button :disabled="TimeFlag" @click="sendCode">{{getMsg}}</el-button>
        </el-form-item>
        <el-tooltip v-model="capsTooltip2" content="大写锁定已打开" placement="right" manual>
            <el-form-item label="新密码" prop="password">
              <el-input prefix-icon="el-icon-lock" show-password ref="password" v-model="forgetForm.password"
                type="password" placeholder="密码" name="password" tabindex="2" autocomplete="on"
                @keyup.native="checkCapslock" @blur="capsTooltip2 = false" />
            </el-form-item>
          </el-tooltip>
      </el-form>

      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="changePassword">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    validPhone,
    validPassword
  } from '@/utils/validate'
  import {
    userLoginApi,
    userInfoApi,
    sendCodeApi,
    userChangePasswordApi
  } from '@/api/users'
  import Vcode from "vue-puzzle-vcode"

  export default {
    components: {
      Vcode
    },
    data() {
      const validatePhone = (rule, value, cb) => {
        if (value.trim() === '') {
          cb(new Error('请输入手机号！'))
        } else if (!validPhone(value)) {
          cb(new Error('请输入合法的手机号！'))
        } else {
          cb()
        }
      }

      const validatePassword = (rule, value, cb) => {
        if (!validPassword(value)) {
          cb(new Error('密码由6-20位数字、字母、下划线、横杠组成！'))
        } else {
          cb()
        }
      }

      return {
        loginForm: {
          phone: '',
          password: ''
        },
        loginRules: {
          phone: [{
            required: true,
            trigger: 'blur',
            validator: validatePhone
          }],
          password: [{
            required: true,
            trigger: 'blur',
            validator: validatePassword
          }],
          code: [{
            required: true,
            message: '请输入验证码',
            trigger: 'blur'
          }]
        },
        forgetRules: {
          phone: [{
            required: true,
            trigger: 'blur',
            validator: validatePhone
          }],
          code: [{
            required: true,
            message: '请输入验证码',
            trigger: 'blur'
          }]
        },
        forgetForm: {
          phone: '',
          code: '',
          password: ''
        },
        capsTooltip: false,
        capsTooltip2: false,
        loading: false,
        // 验证码
        isShow: false,
        dialogFormVisible: false,
        // 发送验证码的倒计时相关变量
        authTime: 60,
        TimeFlag: false,
        getMsg: '获取验证码',
        timer: null
      }
    },
    methods: {
      // 判断用户是否开启大写，并提示
      checkCapslock(e) {
        const {
          key
        } = e
        this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
      },
      login() {
        this.$refs.loginFormRef.validate(valid => {
          if (!valid) return
          this.loading = true
          this.isShow = true
        })
      },
      async success() {
        const {
          data: res
        } = await userLoginApi(this.loginForm)
        // 如果登录失败，提示错误信息，更新验证码，清空文本框的验证码
        if (!res.success) {
          this.$message.error(res.message)
          this.loading = false
          this.isShow = false
          return
        }
        this.$message.success(res.message)
        this.$store.dispatch('setToken', res.data.token)
        this.loading = false
        this.$router.push('/index')
      },
      close() {
        this.isShow = false
        this.loading = false
      },
      sendCode() {
        this.$refs.forgetRef.validateField('phone', async error => {
          if (error) return
          const { data } = await sendCodeApi(this.forgetForm.phone, 2)
          if (!data.success) return this.$message.error(data.message)
          this.$message.success(data.message)
          // 禁用获取验证码按钮
          this.TimeFlag = true
          if (this.authTime > 0) {}
          this.timer = setInterval(() => {
            this.authTime--
            this.getMsg = this.authTime + '秒后再次获取'
            if (this.authTime <= 0) {
              this.TimeFlag = false
              this.authTime = 60
              clearInterval(this.timer)
              this.timer = null
              this.getMsg = '获取验证码'
            }
          }, 1000)
        })
      },
      changePassword() {
        this.$refs.forgetRef.validate(async valid => {
          if (!valid) return
          const { data } = await userChangePasswordApi(this.forgetForm, { code: this.forgetForm.code })
          if (!data.success) return this.$message.error(data.message)
          this.$message.success(data.message)
          this.dialogFormVisible = false
        })
      }
    }
  }
</script>


<style>
  .login-container .login-form .el-input__inner {
    background: #F6F6F6;
    border: 1px solid #EDEDED;
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    color: #3D4059;
  }
</style>

<style lang="less" scoped>
  .login-container {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 400px;

    .login-form {
      padding-top: 15px;
    }

    .signin {
      display: flex;
      justify-content: space-between;
      padding: 0 5px;
      box-sizing: border-box;

      >a {
        line-height: 20px;
        height: 20px;
        font-size: 12px;
        color: #777;
      }
    }

    .other-login {
      text-align: center;

      .svgImg {
        width: 34px;
        cursor: pointer;
      }
    }
  }
</style>