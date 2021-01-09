<template>
  <div class="msg-limiting-surface">
    <Header @user="getUserInfo($event)" />

    <div class="msg-container">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>留言板</span></el-breadcrumb-item>
      </el-breadcrumb>

      <el-card>
        <div class="msg-preface">
          <div class="msg-title">
            <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/bg_news.png" />
            <div class="msg-title-content">
              <span>留言板</span>
              <p>本页可在登录后留言、吐槽、提问，杜绝广告！</p>
            </div>
          </div>
          <img class="clip" src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/item_clip01.png" />
          <img class="pencil" src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/item_pen02.png" />
          <el-divider>欢迎各位大佬吐槽</el-divider>
        </div>

        <div class="comment-send-container">
          <div class="comment-send">
            <div class="user-face">
              <img :src="user.avatar" v-if="user" />
            </div>
            <div class="textarea-container">
              <textarea v-model="message.content" cols="80" rows="5" :placeholder="value" class="ipt-txt"></textarea>
              <button class="comment-submit" @click="leaveMessage">点击发送</button>
            </div>
          </div>
        </div>

        <div>
          <div class="comment-list">
            <div class="list-item" v-for="(item, index) in messages" :key="item.id">
              <!-- 左侧头像 -->
              <div class="user-face">
                <img :src="item.avatar" />
              </div>
              <!-- 右侧 -->
              <div class="con">
                <div class="user">{{item.nickname}}</div>
                <p class="text">{{item.content}}</p>
                <div class="info">
                  <span class="time">{{item.time | dateFormat}}</span>
                  <span class="reply" @click="reply(index, item.nickname, item.level, item.id)">回复</span>
                </div>
                <!-- 回复区域 -->
                <div class="reply-box">
                  <!-- 回复列表 -->
                  <div class="reply-item" v-for="child in item.children" :key="child.id">
                    <a href="javascript:;">
                      <img class="item-avatar" :src="child.avatar" />
                    </a>
                    <div class="reply-con">
                      <div class="user">
                        <span>{{child.nickname}}</span>
                        <span class="text-con">{{child.content}}</span>
                      </div>
                      <div class="info">
                        <span class="time">{{child.time | dateFormat}}</span>
                        <span class="reply" @click="reply(index, child.nickname, child.level, child.id)">回复</span>
                      </div>
                    </div>

                    <!-- 回复的回复 -->
                    <div class="reply-answer-box" style="margin-top: 5px">
                      <div class="answer-item" v-for="grandson in child.children" :key="grandson.id">
                        <a href="javascript:;">
                          <img class="item-avatar" :src="grandson.avatar" />
                        </a>
                        <div class="reply-con">
                          <div class="user">
                            <span>{{grandson.nickname}}</span>
                            <span class="text-con">{{grandson.content}}</span>
                          </div>
                          <div class="info">
                            <span class="time">{{grandson.time | dateFormat}}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="comment-send" v-show="index === peg">
                    <div class="user-face">
                      <img :src="user.avatar" v-if="user" />
                    </div>
                    <div class="textarea-container">
                      <textarea v-model="answer.content" cols="80" rows="5" :placeholder="replyUser"
                        class="ipt-txt"></textarea>
                      <button class="comment-submit" @click="replyMessage">点击发送</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="queryInfo.page" :page-sizes="[3, 5, 8, 10]" :page-size="queryInfo.rows"
              layout="total, sizes, prev, pager, next, jumper" :total="total" background>
            </el-pagination>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import {
    findMessagesApi,
    addMessageApi
  } from '@/api/comment'
  export default {
    components: {
      Header
    },
    data() {
      return {
        message: {
          content: '',
          level: 0,
          parentId: 0
        },
        answer: {
          content: '',
          level: null,
          parentId: null
        },
        user: {},
        value: '欢迎各位大佬评论、留言、吐槽、提问',
        replyUser: '回复 @稻荷：',
        queryInfo: {
          page: 1,
          rows: 5
        },
        messages: [],
        peg: -1,
        total: 0
      }
    },
    created() {
      this.getMessage()
      this.getUserInfo()
    },
    methods: {
      async leaveMessage() {
        const token = window.sessionStorage.getItem('token')
        if (!token || !this.user) return this.$message.error('您尚未登录！')
        if (this.message.content.trim() === '' || this.message.content.trim().length < 1) return this.$message.error(
          '请输入内容！')
        const {
          data
        } = await addMessageApi(this.message)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.message.content = ''
        this.getMessage()
      },
      async replyMessage() {
        const token = window.sessionStorage.getItem('token')
        if (!token || !this.user) return this.$message.error('您尚未登录！')
        if (this.answer.content.trim() === '' || this.answer.content.trim().length < 1) return this.$message.error(
          '请输入内容！')
        const {
          data
        } = await addMessageApi(this.answer)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.answer.content = ''
        this.peg = -1
        this.getMessage()
      },
      async getMessage() {
        const {
          data
        } = await findMessagesApi(this.queryInfo)
        this.messages = data.data.messages.rows
        this.total = data.data.messages.total
      },
      reply(index, name, level, id) {
        this.peg = index
        this.replyUser = '回复 @' + name + '：'
        this.answer.level = level + 1
        this.answer.parentId = id
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getMessage()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getMessage()
      },
      getUserInfo(data) {
        this.user = data
      }
    }
  }
</script>

<style lang="less" scoped>
  @import '@/style/comment/comment.less';

  .msg-limiting-surface {
    background: url(https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/bg_body.jpg) repeat;
  }

  .msg-container {
    width: 1080px;
    margin: 0 auto;
    padding-bottom: 24px;

    .el-breadcrumb {
      height: 29px;
      line-height: 29px;
      padding: 20px 0 0 0;

      >span {
        font-size: 12px;
      }
    }

    .msg-preface {
      position: relative;

      .msg-title {
        position: relative;
        text-align: center;

        .msg-title-content {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -35%);
          font-size: 45px;
          font-family: 'STXingkai';
          color: #333;

          >p {
            height: 30px;
            line-height: 30px;
            font-size: 20px;
          }
        }
      }

      .pencil {
        width: 200px;
        top: 0;
        right: 130px;
        position: absolute;
      }

      .clip {
        position: absolute;
        top: 40px;
        left: 0;
        width: 360px;
      }

      >p {
        font-size: 16px;
        text-align: center;
        color: #444;
      }
    }

    .comment-send-container {
      padding: 15px 0;
    }
  }
</style>