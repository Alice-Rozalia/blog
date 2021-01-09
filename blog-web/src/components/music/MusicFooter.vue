<template>
  <div class="music-fotter-container">
    <aplayer :music="$store.state.music[0]" fixed ref="aplayer" :show-lrc="true" :list="$store.state.music" repeat="repeat-one"></aplayer>
  </div>
</template>

<script>
  import aplayer from 'vue-aplayer'
  export default {
    components: {
      aplayer
    },
    computed: {
      listData() {
        return this.$store.state.music;
      }
    },
    watch: {
      listData(val) {
        this.$refs.aplayer.play()
      }
    }
  }
</script>

<style lang="less" scoped>
  .music-fotter-container {
    height: 66px;
    background: #f2f2f2;
    border-top: 1px solid #ccc;
    .aplayer {
      margin: 0;
    }
  }
</style>