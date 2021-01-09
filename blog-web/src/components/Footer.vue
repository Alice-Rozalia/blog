<template>
  <footer>
    <div class="footer-main">
      <div class="info">
        <div class="title">
          <i class="el-icon-info">
            <span>博客信息</span>
          </i>
        </div>
        <ul class="websiteInfo">
          <li>
            <i class="el-icon-document">
              <span> 文章总数：{{$store.state.total}}篇</span>
            </i>
          </li>
          <li>
            <i class="el-icon-s-comment">
              <span> 留言总数：{{cm_count.messageCount}}条</span>
            </i>
          </li>
          <li>
            <i class="el-icon-chat-dot-round">
              <span> 评论总数：{{cm_count.commentCount}}条</span>
            </i>
          </li>
          <!-- <li>
            <i class="el-icon-time">
              <span id="htmer_time"> 博客运行天数：1天2时间7分5秒</span>
            </i>
          </li> -->
          <!-- <li>
            <i class="el-icon-view">
              <span> 访客总数：12312</span>
            </i>
          </li> -->
        </ul>
      </div>
    </div>
  </footer>
</template>

<script>
  import {
    findMessageAndCommnetApi
  } from '@/api/comment'
  export default {
    data() {
      return {
        cm_count: {}
      }
    },
    created() {
      this.getCMCountTotal()
    },
    methods: {
      async getCMCountTotal() {
        const {
          data
        } = await findMessageAndCommnetApi()
        this.cm_count = data.data.cm_count
      }
    }
  }
</script>

<style lang="less" scoped>
  li {
    list-style: none;
  }

  footer {
    height: 140px;
    background: #333333;
    padding-top: 30px;

    .footer-main {
      width: 1200px;
      margin: 0 auto;
      color: #999;
      font-size: 15px;

      .info {
        width: 310px;
        border-right: 1px solid #7f7f7f;

        .title {
          height: 35px;
          line-height: 35px;
          padding-left: 15px;
        }

        .websiteInfo {
          >li {
            height: 23px;
            line-height: 23px;
          }
        }
      }
    }
  }
</style>