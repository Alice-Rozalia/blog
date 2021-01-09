<template>
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
          <comment-send :replyUser="replyUser" :replyLevel="replyLevel" :replyPId="replyPId" v-show="index === peg" />
        </div>
      </div>
    </div>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.page"
      :page-sizes="[3, 5, 8, 10]" :page-size="queryInfo.rows" layout="total, sizes, prev, pager, next, jumper"
      :total="total" background>
    </el-pagination>
  </div>
</template>

<script>
  import CommentSend from '@/components/CommentSend.vue'
  import {
    findMessagesApi
  } from '@/api/comment'
  export default {
    components: {
      CommentSend
    },
    data() {
      return {
        replyUser: '回复 @稻荷：',
        replyLevel: null,
        replyPId: null,
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
    },
    methods: {
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
        this.replyLevel = level + 1
        this.replyPId = id
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getMessage()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getMessage()
      }
    }
  }
</script>

<style lang="less" scoped>
  
</style>>