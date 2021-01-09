<template>
  <div class="song-list-caontainer scrollbar">
    <div class="head">
      <div class="left-img">
        <img :src="list.coverImgUrl" />
      </div>
      <div class="right-info">
        <div class="right-head-tital">
          <span class="playlist-peg">歌单</span>
          <h4 class="playlist-name">{{list.name}}</h4>
        </div>
        <div class="creator-info">
          <img :src="avatar" />
          <a href="javascript:;" class="nickname">{{nickname}}</a>
          <span class="create-time">{{list.createTime | dateFormat2}}创建</span>
        </div>
        <div class="btn-area">
          <el-button type="danger" icon="el-icon-caret-right" size="small" round>播放全部</el-button>
          <el-button icon="el-icon-folder-add" size="small" round>收藏({{list.subscribedCount | million}})</el-button>
          <el-button icon="el-icon-share" size="small" round>分享({{list.shareCount | million}})</el-button>
          <el-button icon="el-icon-download" size="small" round>下载全部</el-button>
        </div>
        <div class="play-list-info">
          <div class="tag">
            <span>标签：</span>
            <a href="javascript:;" v-for="(tag, index) in list.tags" :key="index">
              {{index === (list.tags.length - 1) ? tag : tag + ' / '}}
            </a>
          </div>
          <div class="songs">
            <span>歌曲：{{list.trackCount}}</span>
            <span style="margin-left: 16px">播放：{{list.playCount | million}}</span>
          </div>
          <div class="intro">
            <i :class="descriptionIcon" class="description-icon" @click="showIntactDescription"
              v-if="descriptionFlag"></i>
            <span class="description">简介：<span>{{description}}</span></span>
          </div>
        </div>
      </div>
    </div>
    <el-table :data="tracks" style="width: 100%; margin: 0" stripe @cell-dblclick="playMusic">
      <el-table-column type="index" label="" align="center"></el-table-column>
      <el-table-column label="操作" width="60" align="center">
        <template>
          <i class="el-icon-star-off" style="margin-right: 8px"></i>
          <i class="el-icon-download"></i>
        </template>
      </el-table-column>
      <el-table-column label="音乐标题" width="240">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="歌手" width="150">
        <template slot-scope="scope">
          {{ scope.row.ar.length != 1 ? scope.row.ar[0].name + ' / ' + scope.row.ar[1].name : scope.row.ar[0].name }}
        </template>
      </el-table-column>
      <el-table-column prop="al.name" label="专辑"></el-table-column>
      <el-table-column label="时长" width="100">
        <template slot-scope="scope">{{ scope.row.dt | durationFormat }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {
    playList,
    check,
    song,
    lyric
  } from '@/api/music/playlist/playlist'
  export default {
    data() {
      return {
        id: this.$route.params.id,
        list: {},
        // 歌曲列表
        tracks: [],
        // 歌单创建者的头像。vue异步获取数据，不提前定义值会报 undefined
        avatar: '',
        // 歌单创建者的昵称
        nickname: '',
        // 简介的折叠图标
        descriptionIcon: 'el-icon-caret-bottom',
        // 简介
        description: '',
        // 简介原始数据
        primevalDescription: '',
        // 简介折叠小图标是否渲染
        descriptionFlag: false,
        // 歌单歌曲列表
        playlistMusic: [],
        // 歌曲数组所需要的参数对象
        playlistMusicParams: {
          title: '',
          artist: '',
          src: '',
          pic: '',
          lrc: ''
        }
      }
    },
    created() {
      this.getPlayList()
    },
    methods: {
      async getPlayList() {
        const { data } = await playList(this.id)
        this.list = data.playlist
        this.tracks = data.playlist.tracks
        this.avatar = data.playlist.creator.avatarUrl
        this.nickname = data.playlist.creator.nickname
        // 处理简介的格式
        if (data.playlist.description.includes('\n')) {
          this.descriptionFlag = true
          this.primevalDescription = data.playlist.description
          this.description = data.playlist.description.substr(0, data.playlist.description.indexOf('\n')) + '...'
        } else if (data.playlist.description.length > 35) {
          this.descriptionFlag = true
          this.primevalDescription = data.playlist.description
          this.description = data.playlist.description.substr(0, 35) + '...'
        } else {
          this.description = data.playlist.description
        }
      },
      // 显示完整的简介
      showIntactDescription() {
        if (this.descriptionIcon === 'el-icon-caret-bottom') {
          this.description = this.primevalDescription
          this.descriptionIcon = 'el-icon-caret-top'
        } else {
          if (this.primevalDescription.includes('\n')) {
            this.description = this.primevalDescription.substr(0, this.primevalDescription.indexOf('\n')) + '...'
          } else {
            this.description = this.primevalDescription.substr(0, 35) + '...'
          }
          this.descriptionIcon = 'el-icon-caret-bottom'
        }
      },
      // 双击播放音乐
      async playMusic(row) {
        // 检验是否有版权
        const { data: result } = await check(row.id)
        if (!result.success) return this.$message.error(result.message)
        // 获取歌曲url
        const { data: songUrl } = await song(row.id)
        this.playlistMusicParams.src = songUrl.data[0].url
        // 获取歌词
        const { data: ly } = await lyric(row.id)
        if (ly.nolyric) {
          this.playlistMusicParams.lrc = '[00:00.00] 纯音乐，请欣赏！'
        } else {
          this.playlistMusicParams.lrc = ly.lrc.lyric
        }
        this.playlistMusicParams.title = row.name
        this.playlistMusicParams.artist = row.ar[0].name
        this.playlistMusicParams.pic = row.al.picUrl

        this.playlistMusic.splice(0, 1)
        this.playlistMusic.push(this.playlistMusicParams)
        this.playlistMusicParams = {}
        this.$store.commit('setMusic', this.playlistMusic)
      }
    }
  }
</script>

<style lang="less">
  .song-list-caontainer {
    .el-table {
      .cell {
        height: 23px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      td,
      th {
        padding: 8px 0;
      }
    }

    .el-progress-bar__innerText {
      font-size: 0;
    }
  }
</style>

<style lang="less" scoped>
  @import '@/style/music/scrollbar.less';

  .song-list-caontainer {
    padding: 30px 0 30px 25px;

    .head {
      display: flex;
      margin-bottom: 30px;

      .left-img {
        width: 185px;
        height: 185px;

        >img {
          width: 100%;
          border-radius: 6px;
        }
      }

      .right-info {
        flex: 1;
        padding-left: 20px;

        .right-head-tital {
          display: flex;
          align-items: center;

          .playlist-peg {
            border: 1px solid #00a4ff;
            border-radius: 3px;
            color: #00a4ff;
            font-size: 13px;
            padding: 1px 3px;
            margin-right: 5px;
          }

          .playlist-name {
            font-size: 20px;
          }
        }

        .creator-info {
          display: flex;
          align-items: center;
          margin-top: 12px;

          >img {
            width: 26px;
            height: 26px;
            border-radius: 50%;
            cursor: pointer;
          }

          .nickname {
            font-size: 13px;
            color: #507daf;
            margin: 0 12px;
          }

          .create-time {
            font-size: 13px;
          }
        }

        .btn-area {
          margin: 12px 0;
        }

        .play-list-info {
          font-size: 14px;

          .tag {
            >a {
              font-size: 14px;
              color: #507daf;
            }
          }

          .songs {
            margin: 4px 0;
          }

          .intro {
            position: relative;

            .description-icon {
              position: absolute;
              top: 5px;
              right: 70px;
              color: #676767;
              cursor: pointer;
            }

            .description {
              white-space: pre-line;
              display: inline-block;
              width: 550px;

              >span {
                line-height: 24px;
                color: #676767;
              }
            }
          }
        }
      }
    }
  }
</style>