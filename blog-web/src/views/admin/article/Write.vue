<template>
  <div class="write-article-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>文章管理</el-breadcrumb-item>
      <el-breadcrumb-item>发表文章</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-form :rules="rules" ref="FormRef" label-width="70px" :model="addArticle">
        <!-- 标题、分类、标签部分 -->
        <el-row :gutter="50">
          <el-col :span="14">
            <el-form-item label="标题" prop="title">
              <el-input size="small" v-model="addArticle.title" clearable placeholder="请输入文章标题！">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分类">
              <el-select v-model="addArticle.cid" size="small" placeholder="请选择文章分类！">
                <el-option v-for="item in catlist" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 发表、赞赏、评论等的开关 -->
        <el-row :gutter="50">
          <el-col :span="7">
            <el-form-item label="转载文章">
              <el-switch v-model="addArticle.shareStatement" active-color="#13ce66">
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="发布">
              <el-switch v-model="addArticle.published" active-color="#13ce66">
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="标签">
              <el-select v-model="selectTags" size="small" multiple placeholder="请选择Tag标签！">
                <el-option v-for="item in taglist" :key="item.id" :label="item.name" :value="item.id">
                  <template>
                    <div class="select-style" :style="{'background': item.color}">{{item.name}}</div>
                  </template>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 文章封面 -->
        <el-form-item label="文章封面">
          <el-upload action="http://8.129.237.3/api/v1/pri/alioss/upload" :on-remove="delImg" :on-error="uploadError"
            :file-list="filelist" :on-success="updateSuccess" :headers="headerObj" :limit="1" list-type="picture">
            <el-button size="mini" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="正文" prop="content">
          <mavon-editor v-model="addArticle.content" :toolbars="toolbars" />
        </el-form-item>
      </el-form>

      <el-button size="small" type="primary" @click="putIn">提交</el-button>
    </el-card>
  </div>
</template>

<script>
  import {
    findCategoriesApi,
    findTagsApi
  } from '@/api/type'
  import {
    deleteImgApi
  } from '@/api/file'
  import {
    addArticleApi,
    findArticleByIdApi,
    editArticleApi
  } from '@/api/article'
  export default {
    data() {
      return {
        addArticle: {
          content: '', //输入的数据
          title: '',
          cid: '',
          shareStatement: false, // 是否为转载文章
          published: true, // 是否发布
          cover: ''
        },
        selectTags: [],
        rules: {
          title: [{
            required: true,
            message: '请输入标题！',
            trigger: 'blur'
          }],
          content: [{
            required: true,
            message: '请输入正文！',
            trigger: 'blur'
          }],
          category: [{
            required: true,
            message: '请选择分类！',
            trigger: 'change'
          }],
          tag: [{
            required: true,
            message: '请选择标签！',
            trigger: 'change'
          }]
        },
        toolbars: {
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          mark: true, // 标记
          superscript: true, // 上角标
          quote: true, // 引用
          ol: true, // 有序列表
          link: true, // 链接
          imagelink: true, // 图片链接
          help: true, // 帮助
          code: true, // code
          subfield: true, // 是否需要分栏
          fullscreen: true, // 全屏编辑
          readmodel: true, // 沉浸式阅读
          /* 1.3.5 */
          undo: true, // 上一步
          trash: true, // 清空
          save: true, // 保存（触发events中的save事件）
          /* 1.4.2 */
          navigation: true // 导航目录
        },
        // 分类列表
        catlist: [],
        taglist: [],
        filelist: [],
        id: this.$route.query.id,
        // 图片上传组件的请求头
        headerObj: {
          Authorization: window.sessionStorage.getItem('token')
        }
      }
    },
    created() {
      this.getCat()
      this.getTags()
      this.getEditArticle()
      this.getEditTags()
    },
    methods: {
      async getCat() {
        const {
          data
        } = await findCategoriesApi()
        this.catlist = data.data.categories.rows
      },
      async getTags() {
        const {
          data
        } = await findTagsApi()
        this.taglist = data.data.tags.rows
      },
      async getEditTags() {
        if (!this.id) return
        const {
          data
        } = await Tags(this.id)
        this.selectTags.push(...data.data.tags)
      },
      async getEditArticle() {
        if (!this.id) return
        const {
          data
        } = await findArticleByIdApi(this.id)
        this.addArticle = {
          ...data.data.article
        }
        this.filelist = [{
          url: this.addArticle.cover
        }]
      },
      // 删除图片
      async delImg(file) {
        const { data } = await deleteImgApi(file.url)
        if (data.success) this.$message.success('该图片已被删除！')
      },
      updateSuccess(response) {
        this.addArticle.cover = response.data.url
      },
      uploadError() {
        this.$message.error('上传失败！')
      },
      async putIn() {
        this.$refs.FormRef.validate(async valid => {
          if (!valid) return;
          let data;
          if (!this.id) {
            data = await addArticleApi(this.addArticle, this.selectTags.toString())
          } else {
            data = await editArticleApi(this.addArticle, this.selectTags.toString())
          }
          if (!data.data.success) return this.$message.error(data.data.message)
          this.$message.success(data.data.message)
          window.sessionStorage.setItem('activePath', '/admin/articles')
          this.$router.push('/admin/articles')
        })
      }
    }
  }
</script>

<style lang="less">
  .write-article-container {
    .el-form--label-top .el-form-item__label {
      padding: 0;
    }
  }
</style>

<style lang="less" scoped>
  .el-select-dropdown__item {
    display: flex;
    align-items: center;
  }

  .select-style {
    width: 100%;
    height: 80%;
    padding-bottom: 5px;
    padding-left: 12px;
    color: #fff;
    border-radius: 5px;
  }
</style>