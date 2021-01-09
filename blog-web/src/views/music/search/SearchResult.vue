<template>
  <div class="music-main-container scrollbar">
    <h3 class="title">找到{{$store.state.searchTotal}}首单曲</h3>

    <el-table :data="$store.state.searchList" style="width: 100%; margin: 0" @cell-dblclick="playMusic" stripe>
      <el-table-column type="index" label=""></el-table-column>
      <el-table-column label="操作" width="60" align="center">
        <template>
          <i class="el-icon-star-off" style="margin-right: 8px"></i>
          <i class="el-icon-download"></i>
        </template>
      </el-table-column>
      <el-table-column label="音乐标题" width="290">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
          <span
            style="margin-left: 5px; color: #9b9b9b">{{ scope.row.alias.length != 0 ? '(' + scope.row.alias[0] + ')' : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="歌手" width="150">
        <template slot-scope="scope">
          {{ scope.row.artists.length != 1 ? scope.row.artists[0].name + ' / ' + scope.row.artists[1].name : scope.row.artists[0].name }}
        </template>
      </el-table-column>
      <el-table-column prop="album.name" label="专辑"></el-table-column>
      <el-table-column label="时长" width="60">
        <template slot-scope="scope">{{ scope.row.duration | durationFormat }}</template>
      </el-table-column>
      <el-table-column label="热度" width="100">
        <template>
          <el-progress :text-inside="true" color="#ccc" :percentage="80"></el-progress>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { check, song, lyric, songDetail } from '@/api/music/playlist/playlist'
  export default {
    data() {
      return {
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
    created() {},
    methods: {
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
          this.playlistMusicParams.lrc = '[00:00.00] 纯音乐，请欣赏!'
        } else {
          this.playlistMusicParams.lrc = ly.lrc.lyric
        }
        // 获取歌曲详情
        const { data: detail } = await songDetail(row.id)
        this.playlistMusicParams.title = row.name
        this.playlistMusicParams.artist = row.artists[0].name
        this.playlistMusicParams.pic = detail.songs[0].al.picUrl

        this.playlistMusic.splice(0, 1)
        this.playlistMusic.push(this.playlistMusicParams)
        this.playlistMusicParams = {}
        this.$store.commit('setMusic', this.playlistMusic)
      }
    }
  }
</script>

<style lang="less">
  .music-main-container {
    background: #fff;
    height: 600px;

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

  .music-main-container {
    .title {
      height: 60px;
      line-height: 60px;
      padding-left: 30px;
      font-weight: 600;
      border-bottom: 1px solid #ddd;
    }
  }
</style>