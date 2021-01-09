<template>
  <div class="comment-send">
    <div class="user-face">
      <img :src="user.avatar" v-if="user" />
    </div>
    <div class="textarea-container">
      <textarea v-model="message.content" cols="80" rows="5" :placeholder="replyUser" class="ipt-txt"></textarea>
      <button class="comment-submit" @click="leaveMessage">点击发送</button>
    </div>
  </div>
</template>

<script>
  import {
    addMessage
  } from '@/api/comment'
  export default {
    props: ['replyUser', 'replyPId', 'replyLevel'],
    data() {
      return {
        message: {
          content: '',
          level: null,
          parentId: null
        },
        user: JSON.parse(window.sessionStorage.getItem('user')),
        value: '欢迎各位大佬评论、留言、吐槽、提问'
      }
    },
    methods: {
      async leaveMessage() {
        const token = window.sessionStorage.getItem('token')
        if (!token || !this.user) return this.$message.error('您尚未登录！')
        if (this.message.content.trim() === '' || this.message.content.trim().length < 1) return this.$message.error('请输入内容！')
        this.message.level = this.replyLevel
        this.message.parentId = this.replyPId
        const { data } = await addMessage(this.message)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.msg = ''
      }
    }
  }
</script>

<style lang="less" scoped>
  @import '@/style/comment/comment.less';
</style>