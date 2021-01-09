<template>
  <div class="music-aside-container">
    <div class="aside-menu">
      <ul>
        <li><router-link to="/music/find_music">发现音乐</router-link></li>
        <li><router-link to="/music/find_music">视频</router-link></li>
        <li><router-link to="/music/find_music">朋友</router-link></li>
        <li><router-link to="/music/find_music">直播</router-link></li>
        <li><router-link to="/music/find_music">私人FM</router-link></li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {}
</script>

<style lang="less" scoped>
  li {
    list-style: none;
  }

  .music-aside-container {
    height: 600px;
    background: #fff;
    border-right: 1px solid #ccc;
    padding: 10px 15px;
    box-sizing: border-box;

    .aside-menu {
      li {
        cursor: pointer;
        border-radius: 5px;
        font-size: 15px;

        &:hover {
          background: #f5f5f6;
        }

        >a {
          display: block;
          padding: 8px;
        }
      }
    }
  }
</style>