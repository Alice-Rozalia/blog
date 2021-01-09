<template>
  <div class="swiper-container">
    <el-carousel type="card" height="230px" :interval="5000">
      <el-carousel-item v-for="item in result" :key="item.id">
        <img :src="item.coverUrl" class="djprogram-pic" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
  import { Recommend } from '@/api/music/findMusic/findMusicPageApi'
  export default {
    data() {
      return {
        result: []
      }
    },
    created() {
      this.getRecommend()
    },
    methods: {
      async getRecommend() {
        const {
          data
        } = await Recommend()
        this.result = data.programs
        // console.log(data);
      }
    }
  }
</script>

<style lang="less">
  .swiper-container {
    .el-carousel__item--card {
      border-radius: 12px;
    }

    .el-carousel__button {
      width: 7px;
      height: 7px;
      border-radius: 50%;
    }

    .el-carousel__indicator.is-active button {
      background-color: #00a4ff;
    }
  }
</style>

<style lang="less" scoped>
  .swiper-container {
    .djprogram-pic {
      width: 100%;
      height: 100%;
    }
  }
</style>