<template>
  <!-- 顶部导航栏 -->
  <nav class="blogNav">
    <div class="wp">
      <!-- logo区域 -->
      <h2>
        <router-link to="/index" class="blogLogo">稻荷</router-link>
      </h2>
      <!-- 导航菜单 -->
      <ul class="navMenu">
        <li v-for="nav in navs" :key="nav.path">
          <router-link :to="nav.path" :class="$route.path.includes(nav.path) ? 'avtive' : ''">{{nav.name}}</router-link>
        </li>
      </ul>
      <!-- 右侧搜索框及登录按钮 -->
      <div class="navRight">
        <!-- 搜索框 -->
        <div class="searchContainer">
          <transition name="slide-fade">
            <input type="text" v-model="key" @keyup.enter="search" placeholder="输入文章标题，回车搜索" class="searchInput"
              v-show="searchInput" ref="inp">
          </transition>
          <button class="el-icon-search searchBtn" @click="searchBtnChange"></button>
        </div>
        <div class="userContainer">
          <span v-if="user === null">
            <router-link to="/login" class="lglink">登录</router-link>
            <router-link to="/register" class="lglink">注册</router-link>
          </span>
          <div class="user-info" v-if="user !== null">
            <span>{{user.nickname}}</span>
            <el-dropdown>
              <img class="user-info-avatar" :src="user.avatar" />
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
  import {
    findArticlesApi
  } from '@/api/article'
  import {
    userInfoApi
  } from '@/api/users'
  import navs from '@/utils/nav'
  export default {
    name: 'Header',
    inject: ['reload'],
    data() {
      return {
        key: '',
        searchInput: false,
        user: null,
        navs: navs
      }
    },
    created() {
      this.getUserInfo()
    },
    methods: {
      // 如果用户登录了，获取用户信息
      async getUserInfo() {
        const token = window.sessionStorage.getItem('token')
        if (!token) return
        try {
          const { data } = await userInfoApi()
          this.user = data.data.userInfo
          this.$emit('user', this.user)
        } catch (error) {
          this.reload()
        }
      },
      async search() {
        const {
          data
        } = await findArticlesApi({
          key: this.key
        })
        this.$store.dispatch('setArticle', data.data.article.rows)
        this.$store.dispatch('setTotal', data.data.article.total)
        this.key = ''
      },
      searchBtnChange() {
        this.searchInput = !this.searchInput
        if (this.searchInput) {
          this.$refs.inp.focus()
        }
        if (this.$route.path !== '/index') {
          return this.$message.warning('请跳转首页，再搜索！')
        }
        if (this.key.trim() !== '') {
          this.search()
        }
      },
      logout() {
        window.sessionStorage.clear()
        this.reload()
      }
    }
  }
</script>

<style lang="less" scoped>
  @import '@/style/header/header.less';
</style>