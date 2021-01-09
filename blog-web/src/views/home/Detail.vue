<template>
  <div>
    <Header @user="getUserInfo($event)" />

    <div class="detail-container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>文章专栏</span></el-breadcrumb-item>
      </el-breadcrumb>

      <div class="article-detail">
        <div class="article">
          <el-card>
            <div>
              <h2 class="article-title">{{article.title}}</h2>
              <ul class="article-menu">
                <li v-if="article.shareStatement">
                  <el-tag type="warning" effect="dark" size="mini">转载</el-tag>
                </li>
                <li v-if="!article.shareStatement">
                  <el-tag type="success" effect="dark" size="mini">原创</el-tag>
                </li>
                <li>
                  <i class="el-icon-time"></i>
                  <span>{{article.createTime | dateFormat}}</span>
                </li>
                <li>
                  <i class="el-icon-user"></i>
                  <span>{{username}}</span>
                </li>
                <li>
                  <i class="el-icon-view"></i>
                  <span>{{article.views}}</span>
                </li>
                <li>
                  <i class="el-icon-folder"></i>
                  <span>{{article.categoryName}}</span>
                </li>
              </ul>
            </div>
            <img :src="article.cover" class="article-cover" />
            <mavon-editor class="md" :value="article.content" :subfield="prop.subfield" :defaultOpen="prop.defaultOpen"
              :toolbarsFlag="prop.toolbarsFlag" :editable="prop.editable" :scrollStyle="prop.scrollStyle" />
            <footer>
              <ul class="article-tags">
                <li v-for="tag in article.tags" :key="tag.id" :style="{'background': tag.color}">
                  <i class="el-icon-collection-tag">{{tag.name}}</i>
                </li>
              </ul>
            </footer>

            <div class="comment-send-container">
              <div class="comment-send">
                <div class="user-face">
                  <img :src="user.avatar" v-if="user" />
                </div>
                <div class="textarea-container">
                  <textarea v-model="comment.content" cols="80" rows="5" :placeholder="value"
                    class="ipt-txt"></textarea>
                  <button class="comment-submit" @click="leaveComment">点击发送</button>
                </div>
              </div>
            </div>

            <div class="comment-list">
              <div class="list-item" v-for="(item, index) in comments" :key="item.id">
                <!-- 左侧头像 -->
                <div class="user-face">
                  <img :src="item.avatar" />
                </div>
                <!-- 右侧 -->
                <div class="con">
                  <div class="user">{{item.nickname}}</div>
                  <p class="text">{{item.content}}</p>
                  <div class="info">
                    <span class="time">{{item.commentTime | dateFormat}}</span>
                    <span class="reply" @click="reply(index, item.nickname, item.id)">回复</span>
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
                          <span class="time">{{child.commentTime | dateFormat}}</span>
                          <span class="reply" @click="reply(index, child.nickname, child.id)">回复</span>
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
                              <span class="time">{{grandson.commentTime | dateFormat}}</span>
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
                        <button class="comment-submit" @click="replyComment">点击发送</button>
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
          </el-card>
        </div>

        <!-- 右侧小栏目 -->
        <Aside />
      </div>
    </div>

    <el-backtop />
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import Aside from '@/components/Aside.vue'
  import {
    findArticleByIdApi
  } from '@/api/article'
  import {
    findCommentsByArticleIdApi,
    addCommentApi
  } from '@/api/comment'
  export default {
    components: {
      Header,
      Aside
    },
    computed: {
      prop() {
        let data = {
          subfield: false, // 单双栏模式
          defaultOpen: 'preview', //edit： 默认展示编辑区域 ， preview： 默认展示预览区域 
          editable: false, // 是否允许编辑
          toolbarsFlag: false,
          scrollStyle: true,
        }
        return data
      }
    },
    data() {
      return {
        id: this.$route.query.id,
        article: {},
        username: '',
        msg: '',
        value: '发条友善的评论',
        comment: {
          content: '',
          aid: this.$route.query.id,
          parentId: 0
        },
        answer: {
          content: '',
          aid: this.$route.query.id,
          parentId: null
        },
        user: {},
        value: '欢迎各位大佬评论、留言、吐槽、提问',
        replyUser: '回复 @稻荷：',
        queryInfo: {
          page: 1,
          rows: 5,
          aid: this.$route.query.id
        },
        peg: -1,
        total: 0,
        comments: []
      }
    },
    created() {
      this.getArticleDetail()
      this.getComment()
    },
    methods: {
      async getArticleDetail() {
        if (!this.id) return this.$router.push('/index')
        const {
          data
        } = await findArticleByIdApi(this.id)
        if (!data.success) {
          this.$router.push('/index')
          this.$message.warning('该篇文章不存在！')
        }
        this.article = data.data.article
        this.username = data.data.article.author.nickname
      },
      async leaveComment() {
        const token = window.sessionStorage.getItem('token')
        if (!token || !this.user) return this.$message.error('您尚未登录！')
        if (this.comment.content.trim() === '' || this.comment.content.trim().length < 1) return this.$message.error(
          '请输入内容！')
        const {
          data
        } = await addCommentApi(this.comment)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.comment.content = ''
        this.getComment()
      },
      async replyComment() {
        const token = window.sessionStorage.getItem('token')
        if (!token || !this.user) return this.$message.error('您尚未登录！')
        if (this.answer.content.trim() === '' || this.answer.content.trim().length < 1) return this.$message.error(
          '请输入内容！')
        const {
          data
        } = await addCommentApi(this.answer)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.answer.content = ''
        this.peg = -1
        this.getComment()
      },
      async getComment() {
        const {
          data
        } = await findCommentsByArticleIdApi(this.queryInfo)
        this.comments = data.data.comments.rows
        this.total = data.data.comments.total
      },
      reply(index, name, id) {
        this.peg = index
        this.replyUser = '回复 @' + name + '：'
        this.answer.parentId = id
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getComment()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getComment()
      },
      getUserInfo(data) {
        this.user = data
      }
    }
  }
</script>

<style lang="less" scoped>
  @import '@/style/comment/comment.less';

  li {
    list-style: none;
  }

  .detail-container {
    width: 1200px;
    margin: 0 auto;

    .el-breadcrumb {
      height: 29px;
      line-height: 29px;
      padding: 20px 0;

      >span {
        font-size: 12px;
      }
    }

    .article-detail {
      display: flex;
      justify-content: space-between;

      .article {
        width: 890px;

        .article-title {
          font-size: 24px;
          color: #5c5c5c;
          text-align: center;
          margin: 5px 0 15px;
        }

        .article-cover {
          width: 100%;
        }

        .article-menu {
          display: flex;
          justify-content: center;
          align-items: center;
          border-bottom: 1px solid #e6e6e6;
          margin-bottom: 15px;
          padding-bottom: 8px;

          li {
            color: #727272;
            font-size: 14px;
            padding: 0 10px;

            >span {
              margin-left: 5px;
            }
          }
        }

        .article-tags {
          display: flex;
          font-size: 14px;
          color: #fff;
          margin-top: 15px;
          padding-bottom: 8px;
          margin-bottom: 30px;
          border-bottom: 1px solid #e6e6e6;

          li {
            padding: 3px 5px;
            border-radius: 3px;
            margin-right: 5px;
          }
        }
      }
    }
  }
</style>