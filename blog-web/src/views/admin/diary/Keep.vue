<template>
  <div class="editor-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日记管理</el-breadcrumb-item>
      <el-breadcrumb-item>记录日记</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-divider>
        <i class="el-icon-notebook-1"></i>
        <span class="title">日记</span>
      </el-divider>
      <quill-editor v-model="content"></quill-editor>
      <el-button type="primary" @click="putIn">提交</el-button>
    </el-card>
  </div>
</template>

<script>
  import {
    addDiaryApi,
    findByIdApi,
    editDiaryApi
  } from '@/api/diary'
  export default {
    data() {
      return {
        content: '',
        id: this.$route.query.id
      }
    },
    created() {
      this.findDiaryById()
    },
    methods: {
      async putIn() {
        if (this.content.trim() === '') return this.$message.warning('内容不能为空！')

        let data;
        if (!this.id) {
          data = { data } = await addDiaryApi(this.content)
        } else {
          data = { data } = await editDiaryApi({ id: this.id, content: this.content })
        }
        if (!data.data.success) return this.$message.error(data.data.message)
        this.$message.success(data.data.message)
        window.sessionStorage.setItem('activePath', '/admin/diarys')
        this.$router.push('/admin/diarys')
      },
      async findDiaryById() {
        if (!this.id) return
        const {
          data
        } = await findByIdApi(this.id)
        if (!data.success) return this.$message.error(data.message)
        this.content = data.data.diary.content
      }
    }
  }
</script>

<style lang="less" scoped>
  .editor-container {
    .title {
      display: inline-block;
      margin-left: 8px;
    }

    .el-button {
      margin-top: 15px;
    }
  }
</style>