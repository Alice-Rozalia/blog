<template>
  <el-container class="admin-container">
    <!-- 左-侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'">

      <!-- 侧边栏菜单区域 -->
      <el-menu background-color="#304156" text-color="#fff" active-text-color="#00a4ff" unique-opened
        :collapse="isCollapse" :collapse-transition="false" router :default-active="activePath">
        <el-submenu :index="item.id" v-for="item in menulist" :key="item.id">
          <!-- 一级菜单的模板区域 -->
          <template slot="title">
            <!-- 图标 -->
            <i :class="item.icon"></i>
            <!-- 文本 -->
            <span>{{item.menuName}}</span>
          </template>

          <!-- 二级菜单 -->
          <el-menu-item :index="subitem.path" v-for="subitem in item.children" :key="subitem.id"
            @click="saveNavState(subitem.path)">
            <template slot="title">
              <!-- 图标 -->
              <i class="el-icon-menu"></i>
              <!-- 文本 -->
              <span>{{subitem.menuName}}</span>
            </template>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>

    <!-- 右-页面主体区域 -->
    <el-container>
      <!-- 头部区域 -->
      <el-header>
        <!-- 折叠菜单 -->
        <i class="el-icon-s-fold zhedie" @click="isCollapse = !isCollapse"></i>
        <div class="title">
          <span>博客后台管理系统</span>
        </div>
        <div class="user">
          <span class="username">{{userInfo.nickname}}</span>
          <el-dropdown>
            <div class="person-photo">
              <img :src="userInfo.avatar" class="user-avatar">
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link to="/index">前台首页</router-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <!-- 右侧内容主体 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import {
    findMenuApi,
    userInfoApi
  } from '@/api/users'

  export default {
    data() {
      return {
        // 菜单列表
        menulist: [],
        // 是否折叠菜单
        isCollapse: false,
        // 被激活的链接地址
        activePath: 'admin',
        // 用户信息
        userInfo: {}
      }
    },
    created() {
      this.getUserInfo()
      this.getMenus()
      this.getActive()
    },
    methods: {
      async getMenus() {
        const { data } = await findMenuApi()
        this.menulist = data.data.menus
      },
      // 获取用户信息
      async getUserInfo() {
        try {
          const { data } = await userInfoApi()
          this.userInfo = data.data.userInfo
          if (!this.userInfo.isAdmin) {
            this.$router.push('/login')
          }
        } catch (error) {
          this.$router.push('/index')
        }
      },
      // 保存链接的激活状态
      saveNavState(activePath) {
        window.sessionStorage.setItem('activePath', activePath)
        this.activePath = activePath
      },
      // 获取链接激活状态
      getActive() {
        this.activePath = window.sessionStorage.getItem('activePath')
        if (this.activePath === null) {
          this.activePath = 'admin'
        }
      },
      // 退出登陆
      logout() {
        window.sessionStorage.clear()
        this.$router.push('/login')
      }
    }
  }
</script>

<style lang="less" scoped>
  @import '@/style/admin/admin.less';
</style>