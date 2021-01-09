<template>
  <div class="exclusive-container">
    <div class="exclusive-list" v-for="item in list" :key="item.id">
      <img :src="item.sPicUrl" />
      <p class="name">{{ item.name }}</p>
    </div>
  </div>
</template>

<script>
  import { Privatecontent } from '@/api/music/findMusic/findMusicPageApi'
  export default {
    data() {
      return {
        list: []
      }
    },
    created() {
      this.getExclusive()
    },
    methods: {
      async getExclusive() {
        const { data } = await Privatecontent()
        this.list = data.result
      }
    }
  }
</script>

<style lang="less" scoped>
  .exclusive-container {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;

    .exclusive-list {
      width: 270px;
      height: 200px;

      >img {
        width: 100%;
      }

      .name {
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        font-size: 14px;
      }
    }
  }
</style>