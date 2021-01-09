<template>
  <div class="music-head">
    <div class="head-title">
      <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/logo.png" />
      <span>星之声</span>
    </div>
    <div class="search-box">
      <input type="text" v-model="key" @keyup.enter="search" class="music-search" />
      <i class="el-icon-search search-icon" @click="search"></i>
    </div>
    <div class="head-menu-list">
      <div class="head-menu-user">
        <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/avatar.jpg" />
        <span>稻荷</span>
        <i class="el-icon-caret-bottom bottom"></i>
        <span>开通VIP</span>
        <i class="el-icon-s-tools set"></i>
      </div>
      <div class="head-menu-msg">
        <i class="el-icon-message"></i>
        <i class="el-icon-message"></i>
      </div>
      <div class="head-menu-system">
        <i class="el-icon-minus"></i>
        <i class="el-icon-full-screen"></i>
        <i class="el-icon-close"></i>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    searchMusic
  } from '@/api/music/search'
  export default {
    data() {
      return {
        key: ''
      }
    },
    methods: {
      async search() {
        if (this.key.trim() === '') return this.key = ''
        const { data } = await searchMusic(this.key)
        this.key = ''
        // console.log(data);
        this.$store.commit('setSearchList', { songs: data.result.songs, total: data.result.songCount })
        if (this.$route.path !== '/music/search') {
          this.$router.push('/music/search')
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  .music-head {
    display: flex;
    align-items: center;
    height: 60px;
    background: #00a4ff;
    padding: 0 12px;

    .head-title {
      display: flex;
      align-items: center;
      width: 180px;
      font-size: 22px;
      font-family: '华文行楷';
      color: #fff;

      >img {
        height: 50px;
      }
    }

    .search-box {
      position: relative;

      .music-search {
        width: 180px;
        height: 25px;
        outline: none;
        border: none;
        border-radius: 20px;
        background: rgba(255, 255, 255, 0.3);
        color: #fff;
        padding: 0 25px 0 12px;
        font-size: 15px;
      }

      .search-icon {
        position: absolute;
        top: 50%;
        right: 8px;
        transform: translateY(-50%);
        color: #fff;
        font-size: 15px;
        cursor: pointer;
      }
    }

    .head-menu-list {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      flex: 1;

      .head-menu-user {
        display: flex;
        align-items: center;

        >img {
          width: 42px;
          border-radius: 50%;
        }

        >span {
          color: #fff;
          font-size: 14px;
          margin-left: 8px;
          cursor: pointer;
        }

        .bottom {
          color: #fff;
          margin: 2px 5px 0 3px;
          cursor: pointer;
        }

        .set {
          color: #fff;
          font-size: 18px;
          margin-left: 18px;
          cursor: pointer;
        }
      }

      .head-menu-msg {
        display: flex;
        justify-content: space-between;
        width: 50px;
        color: #fff;
        border-right: 1px solid #fff;
        padding: 0 12px;
        margin-right: 20px;

        >i {
          cursor: pointer;
        }
      }

      .head-menu-system {
        display: flex;
        justify-content: space-between;
        width: 90px;
        color: #fff;

        >i {
          cursor: pointer;
        }
      }
    }
  }
</style>