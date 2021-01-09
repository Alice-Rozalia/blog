<template>
  <div class="limiting-surface">
    <div class="register-container">
      <el-card>
        <el-divider>
          <h1>注册</h1>
        </el-divider>

        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form"
          label-position="left" autocomplete="on">
          <el-form-item prop="phone">
            <el-input prefix-icon="el-icon-mobile-phone" ref="phone" type="text" v-model="registerForm.phone" name="phone"
              autocomplete="on" placeholder="手机号" tabindex="1" />
          </el-form-item>

          <el-tooltip v-model="capsTooltip" content="大写锁定已打开" placement="right" manual>
            <el-form-item prop="password">
              <el-input prefix-icon="el-icon-lock" show-password ref="password" v-model="registerForm.password"
                type="password" placeholder="密码" name="password" tabindex="2" autocomplete="on"
                @keyup.native="checkCapslock" @blur="capsTooltip = false" @keyup.enter.native="register" />
            </el-form-item>
          </el-tooltip>

          <el-form-item prop="nickname">
            <el-input prefix-icon="el-icon-user" ref="nickname" type="text" v-model="registerForm.nickname"
              name="nickname" autocomplete="on" placeholder="昵称" tabindex="3" />
          </el-form-item>

          <el-form-item prop="code">
            <el-input prefix-icon="el-icon-key" style="width: 50%;margin-right: 20px" type="text"
              v-model="registerForm.code" name="code" autocomplete="on" placeholder="短信验证码" tabindex="4" />
            <el-button :disabled="TimeFlag" @click="getCode">{{getMsg}}</el-button>
          </el-form-item>

          <el-form-item style="width:100%">
            <el-button :loading="loading" type="primary" style="width:100%; border: none" v-on:click="register">注册
            </el-button>
          </el-form-item>
        </el-form>

        <div class="signin">
          <router-link to="/login">已有账号？去登录</router-link>
        </div>
      </el-card>

      <!-- 验证码 -->
      <Vcode :show="isShow" @success="success" @close="close" :canvasWidth="500" :canvasHeight="350" />
    </div>
  </div>
</template>

<script>
  import {
    validPhone,
    validPassword
  } from '@/utils/validate'
  import Vcode from "vue-puzzle-vcode"
  import {
    sendCodeApi,
    userRegisterApi,
    checkApi
  } from '@/api/users'
  export default {
    components: {
      Vcode
    },
    data() {
      const validatePhone = async (rule, value, cb) => {
        if (value.trim() === '') {
          cb(new Error('请输入手机号！'))
        }
        if (!validPhone(value)) {
          cb(new Error('请输入合法的手机号！'))
        }
        const { data } = await checkApi(value, 2)
        if (data.code === 1253) {
          cb(new Error(data.message))
        }
        cb()
      }

      const validatePassword = (rule, value, cb) => {
        if (!validPassword(value)) {
          cb(new Error('密码由6-16位数字、字母、下划线、横杠组成！'))
        } else {
          cb()
        }
      }

      const validateNickname = async (rule, value, cb) => {
        if (value.trim() === '') {
          cb(new Error('请输入昵称！'))
        }
        const { data } = await checkApi(value, 1)
        if (data.code === 1254) {
          cb(new Error(data.message))
        }
        cb()
      }

      return {
        registerForm: {
          phone: '',
          password: '',
          nickname: '',
          code: ''
        },
        registerRules: {
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
          }],
          nickname: [{
            required: true,
            trigger: 'blur',
            validator: validateNickname
          }, {
            min: 2,
            max: 16,
            message: '长度在 2 到 16 个字符',
            trigger: 'blur'
          }]
        },
        loading: false,
        capsTooltip: false,
        // 验证码
        isShow: false,
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
      getCode() {
        this.$refs.registerFormRef.validateField('phone', error => {
          if (!error) {
            this.isShow = true
          }
        })
      },
      async success() {
        // 获取邮箱验证码
        const { data } = await sendCodeApi(this.registerForm.phone, 1)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.isShow = false
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
      },
      close() {
        this.isShow = false
      },
      register() {
        this.loading = true
        this.$refs.registerFormRef.validate(async valid => {
          if (!valid) return
          const { data } = await userRegisterApi(this.registerForm, { code: this.registerForm.code })
          if (!data.success) {
            this.isShow = false
            return this.$message.error(data.message)
          }
          this.$message.success(data.message)
          this.$router.push('/login')
        })
        this.loading = false
      }
    }
  }
</script>

<style lang="less" scoped>
  .register-container {
    position: fixed;
    top: 40%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 400px;

    .register-form {
      padding-top: 15px;
    }

    .signin {
      height: 15px;
      padding: 0 5px;
      box-sizing: border-box;

      >a {
        float: right;
        line-height: 20px;
        height: 20px;
        font-size: 12px;
        color: #777;
      }
    }
  }
</style>