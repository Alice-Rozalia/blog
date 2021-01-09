<template>
  <div class="thing-limiting-surface">
    <Header />

    <div class="thing-container">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>点点滴滴</span></el-breadcrumb-item>
      </el-breadcrumb>

      <div class="main-container">
        <h2><i class="el-icon-time"> 时光轴——记录生活点点滴滴</i></h2>
        <div class="thing">
          <el-timeline>
            <el-timeline-item v-for="item in diarylist" :key="item.id" :timestamp="item.createTime | dateFormat"
              color="#31c27c" placement="top">
              <div class="thing-content-container">
                <div v-html="item.content" class="thing-content"></div>
              </div>
            </el-timeline-item>
          </el-timeline>

          <!-- 分页区域 -->
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="queryInfo.page" :page-size="queryInfo.rows" background layout="total, prev, pager, next"
            :total="total">
          </el-pagination>
        </div>
      </div>

      <div class="corner">
        <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/img_story_title.png" />
      </div>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import {
    findDiarysApi
  } from '@/api/diary'
  export default {
    components: {
      Header
    },
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 3
        },
        total: 0,
        diarylist: [],
        h: 300
      }
    },
    created() {
      this.getDiarys()
    },
    methods: {
      async getDiarys() {
        const {
          data
        } = await findDiarysApi(this.queryInfo)
        this.diarylist = data.data.diarys.rows
        this.total = data.data.diarys.total
        this.diarylist[0].content.replace(/<[^>]+>/g, '').length
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getDiarys()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getDiarys()
      }
    }
  }
</script>

<style lang="less">
  .thing-container {
    .el-timeline-item__timestamp.is-top {
      height: 30px;
      line-height: 30px;
      color: #fff;
      text-align: center;
      background: url(https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/bg_2nd_title.png) no-repeat;
      background-position: center 100%;
      background-size: contain;
    }
  }
</style>

<style lang="less" scoped>
  .thing-limiting-surface {
    height: 100%;
    background: url(https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/bg_body.jpg) repeat;
  }

  .thing-container {
    width: 1160px;
    margin: 0 auto;
    position: relative;

    .corner {
      position: absolute;
      top: 48px;
      left: -56px;
      transform: rotate(5deg);
    }

    .el-breadcrumb {
      height: 29px;
      line-height: 29px;
      padding: 20px 0 0 0;

      >span {
        font-size: 12px;
      }
    }

    .main-container {
      background: url(https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/bg_note_pink_format.jpg) no-repeat;
      height: 760px;
      padding: 50px 50px 0 0;
      box-sizing: border-box;

      >h2 {
        height: 50px;
        line-height: 50px;
        font-size: 26px;
        text-align: right;
        padding-right: 80px;
        margin-bottom: 12px;
      }

      .thing {
        width: 480px;
        float: right;

        .el-timeline {
          height: 520px;
        }

        .thing-content-container {
          width: 410px;
        }
      }
    }
  }
</style>