<template>
  <!-- 右侧小栏目 -->
  <div class="mainRight">
    <div class="developerInfo card-box bg1">
      <div class="info">
        <!-- 头像 -->
        <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/avatar.jpg" />
        <!-- 昵称 -->
        <div>
          <p class="nickname">稻荷</p>
          <p class="location">
            <i class="el-icon-location-outline">
              <span>广西-南宁</span>
            </i>
          </p>
        </div>
      </div>
      <div class="contact">
        <el-popover placement="top" width="150" trigger="hover">
          <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/QQ.png" width="150" />
          <img slot="reference" class="svgImg" src="../assets/QQ.svg" />
        </el-popover>
        <el-popover placement="top" width="150" trigger="hover" content="...">
          <img slot="reference" class="svgImg" src="../assets/wx.svg" />
        </el-popover>
        <el-popover placement="top" width="150" trigger="hover" content="2654006709@qq.com">
          <img slot="reference" class="svgImg" src="../assets/email.svg" />
        </el-popover>
      </div>
    </div>
    <div class="hotArticle card-box2 bg4">
      <div class="title">
        <img src="../assets/hot.svg" />
        <span>热文排行</span>
      </div>
      <ul class="hotArtList">
        <li v-for="(article, index) in articles" :key="article.id">
          <span class="badge" :class="index < 2 ? 'bg2' : 'bg3'">{{index + 1}}</span>
          <a :href="'/detail?id=' + article.id">{{ article.title }}</a>
          <span class="viewAndGood">—— {{article.views}}阅</span>
        </li>
      </ul>
    </div>
    <div class="ArticleTag card-box">
      <div class="title" style="color: #00a4ff; border-color: #00a4ff">
        <i class="el-icon-collection-tag"></i>
        <span>标签云</span>
      </div>
      <div class="tagsContainer">
        <ul>
          <li v-for="tag in taglist" :key="tag.id"><a href="javascript:;"
              :style="{'background': tag.color}">{{tag.name}}</a></li>
        </ul>
      </div>
    </div>
    <div class="progress card-box2 bg3">
      <div class="title">
        <i class="el-icon-s-promotion">
          <span style="margin-left: 10px">一路走来</span>
        </i>
      </div>
      <div style="padding: 12px 0 0 15px">
        <el-timeline>
          <el-timeline-item v-for="diary in diarylist" :key="diary.id" :timestamp="diary.createTime | dateFormat">
            <span style="color: #fff !important" v-html="diary.content"></span>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    <div class="blogInfo card-box bg6">
      <div class="title">
        <i class="el-icon-share">
          <span>友情链接</span>
        </i>
      </div>
      <ul class="share">
        <li v-for="link in linklist" :key="link.id">
          <a :href="link.link">{{link.name}}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  import {
    findTagsApi
  } from '@/api/type'
  import {
    findFriendlyLinkApi
  } from '@/api/link'
  import {
    findDiarysApi
  } from '@/api/diary'
  import {
    hotApi
  } from '@/api/article'
  export default {
    name: 'Aside',
    data() {
      return {
        diarylist: [],
        taglist: [],
        linklist: [],
        articles: [],
        count: {
          tag: 0,
          message: 0,
          comment: 0
        }
      }
    },
    created() {
      this.getHotArticle()
      this.getTagList()
      this.getLinkList()
      this.getDiarys()
    },
    methods: {
      async getHotArticle() {
        const { data } = await hotApi()
        this.articles = data.data.hot_article
      },
      async getTagList() {
        const {
          data
        } = await findTagsApi()
        this.taglist = data.data.tags.rows
        this.count.tag = data.data.tags.total
      },
      async getLinkList() {
        const {
          data
        } = await findFriendlyLinkApi()
        this.linklist = data.data.links.rows
      },
      async getDiarys() {
        const {
          data
        } = await findDiarysApi({
          page: 1,
          rows: 3
        })
        this.diarylist = data.data.diarys.rows
      }
    }
  }
</script>

<style>
  .el-timeline-item__timestamp {
    color: #fff;
  }
</style>

<style lang="less" scoped>
  @import '@/style/validate.less';
  @import '@/style/aside/aside.less';

  li {
    list-style: none;
  }

  .mainRight {
    width: 290px;
    margin: 0;

    .developerInfo {
      &::after {
        border-top: 15px solid @color8;
        border-left: 10px solid @color4;
      }
    }

    .hotArticle {
      &::before {
        border-right: 10px solid @color8;
      }

      &::after {
        border-bottom: 15px solid @color8;
      }
    }

    .ArticleTag {
      background: rgba(255, 255, 255, 0.6);

      &::after {
        border-left: 10px solid @color3;
        border-top: 15px solid #666;
      }
    }

    .progress {
      &::before {
        border-right: 10px solid #054683;
      }

      &::after {
        border-bottom: 15px solid #054683;
      }
    }
  }
</style>