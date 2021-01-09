<template>
  <el-card shadow="hover">
    <div class="items">
      <div class="articleItemLeft">
        <img :src="article.cover" class="articleCover" />
      </div>
      <div class="articleItemRight">
        <div>
          <h3 class="articleTitle" @click="goDetail(article.id)">{{ article.title }}</h3>
        </div>
        <div class="articleAbstract">
          <p v-html="article.content.length > 170 ? article.content.replace(/<[^>]+>/g, '').substr(0, 170) + '...' : article.content.replace(/<[^>]+>/g, '')"></p>
        </div>
      </div>
    </div>
    <div class="articleFooter">
      <div style="width: 80%">
        <i class="el-icon-time">
          <span>{{ article.createTime | dateFormat2 }}</span>
        </i>
        <i class="el-icon-user-solid">
          <span>{{ article.author.nickname }}</span>
        </i>
        <div class="typaAndTag">
          <i class="el-icon-folder">
            <span>{{ article.categoryName }}</span>
          </i>
          <i class="el-icon-collection-tag">
            <span v-for="val in article.tags" :key="val.id" :style="{'color': val.color}">{{val.name}}</span>
          </i>
        </div>
      </div>
      <div style="width: 15%">
        <i class="el-icon-chat-dot-round">
          <span>{{ article.commentCount }}</span>
        </i>
        <i class="el-icon-view">
          <span>{{ article.views }}</span>
        </i>
      </div>
    </div>
  </el-card>
</template>

<script>
  export default {
    name: 'ArticleItem',
    props: ['article'],
    methods: {
      goDetail(id) {
        this.$router.push({
          path: '/detail',
          query: {
            id: id
          }
        })
      }
    }
  }
</script>

<style lang="less" scoped>
  .items {
    display: flex;

    .articleItemLeft {
      width: 25%;

      .articleCover {
        width: 100%;
        height: auto;
      }
    }

    .articleItemRight {
      width: 73%;
      padding-left: 2%;

      .articleTitle {
        display: inline;
        font-size: 18px;
        color: #555;
        cursor: pointer;
        &:hover {
          color: #00a4ff;
        }
      }

      .articleAbstract {
        >p {
          font-size: 14px !important;
          margin-top: 10px;
          text-indent: 2em;
        }
      }
    }
  }

  .articleFooter {
    display: flex;
    justify-content: space-between;
    margin-top: 5px;
    font-size: 13px;
    color: #a6a6a6;

    i {
      padding-right: 15px;

      >span {
        padding-left: 5px;
      }
    }

    .typaAndTag {
      display: inline-block;
      width: 460px;
      color: #6ad1dd;
    }
  }
</style>