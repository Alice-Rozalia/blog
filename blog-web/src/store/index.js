import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as mutations from './mutations'
import * as actions from './actions'

Vue.use(Vuex)

const state = {
  token: '',
  // 搜索到的歌曲列表
  searchList: [],
  // 搜索到的歌曲总数
  searchTotal: 0,
  music: [{
    title: 'lear earth',
    artist: '天門',
    src: 'http://m8.music.126.net/20201103095142/832a699c42c7a57a2a9f7c2b8b2ebc5a/ymusic/4d28/cbbf/d501/ae5b308e786ee58c413570770d222b21.mp3',
    pic: 'https://p1.music.126.net/tSRITWmM45KmvQqLk9WOaA==/3275445140058616.jpg',
    lrc: '[00:00.00] 纯音乐，请欣赏！'
  }],
  // 是否授权
  isAuthenticate: false,
  // 登录用户的信息
  user: null,
  // 文章列表
  articles: [],
  // 文章总数
  total: 0
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})