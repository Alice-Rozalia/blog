import Vue from 'vue'
import VueRouter from 'vue-router'

const Login = () => import( /* webpackChunkName:"com_login" */ '@/views/Login.vue')

const Home = () => import( /* webpackChunkName:"home_login" */ '@/views/home/Home.vue')
const Index = () => import( /* webpackChunkName:"home_index" */ '@/views/home/Index.vue')
const Friends = () => import( /* webpackChunkName:"home_friends" */ '@/views/home/Friends.vue')
const Resource = () => import( /* webpackChunkName:"home_resource" */ '@/views/home/Resource.vue')
const Detail = () => import( /* webpackChunkName:"home_detail" */ '@/views/home/Detail.vue')
const Thing = () => import( /* webpackChunkName:"home_thingl" */ '@/views/home/Thing.vue')
const About = () => import( /* webpackChunkName:"home_about" */ '@/views/home/About.vue')
const Message = () => import( /* webpackChunkName:"home_msg" */ '@/views/home/Message.vue')
const Register = () => import( /* webpackChunkName:"home_register" */ '@/views/home/Register.vue')

const Music = () => import( /* webpackChunkName:"home_music" */ '@/views/music/Index.vue')
const Search = () => import( /* webpackChunkName:"home_music_search" */ '@/views/music/search/SearchResult.vue')
const FindMusic = () => import( /* webpackChunkName:"home_music_findMusic" */ '@/views/music/find/FindMusic.vue')
const SongList = () => import( /* webpackChunkName:"home_music_songList" */ '@/views/music/songlist/List.vue')

const Admin = () => import( /* webpackChunkName:"admin_admin" */ '@/views/admin/Admin.vue')
const AdminIndex = () => import( /* webpackChunkName:"admin_admin_index" */ '@/views/admin/Index.vue')
const Information = () => import( /* webpackChunkName:"admin_admin_information" */ '@/views/admin/user/Information.vue')
const Users = () => import( /* webpackChunkName:"admin_user_list" */ '@/views/admin/user/List.vue')

const Tag = () => import( /* webpackChunkName:"admin_type_tag" */ '@/views/admin/type/Tag.vue')
const Category = () => import( /* webpackChunkName:"admin_type_type" */ '@/views/admin/type/Category.vue')

const Share = () => import( /* webpackChunkName:"admin_link_share" */ '@/views/admin/link/Share.vue')
const Resources = () => import( /* webpackChunkName:"admin_link_resource" */ '@/views/admin/link/Resource.vue')

const Keep = () => import( /* webpackChunkName:"admin_diary_keep" */ '@/views/admin/diary/Keep.vue')
const Diarys = () => import( /* webpackChunkName:"admin_diary_diarys" */ '@/views/admin/diary/Diarys.vue')

const Write = () => import( /* webpackChunkName:"admin_article_write" */ '@/views/admin/article/Write.vue')
const Article = () => import( /* webpackChunkName:"admin_article_article" */ '@/views/admin/article/Article.vue')

const Comment = () => import( /* webpackChunkName:"admin_reply_comment" */ '@/views/admin/reply/Comment.vue')
const Msg = () => import( /* webpackChunkName:"admin_reply_message" */ '@/views/admin/reply/Message.vue')

const LoginLog = () => import( /* webpackChunkName:"admin_log_login" */ '@/views/admin/log/LoginLog.vue')
const Log = () => import( /* webpackChunkName:"admin_log_log" */ '@/views/admin/log/Log.vue')

Vue.use(VueRouter)

const routes = [{
    path: '/',
    name: '博客门户',
    component: Home
  },
  {
    path: '/login',
    name: '登录页',
    component: Login
  },
  {
    path: '/register',
    name: '注册页',
    component: Register
  },
  {
    path: '/index',
    name: '首页',
    component: Index
  },
  {
    path: '/detail',
    name: '文章详情',
    component: Detail
  },
  {
    path: '/friends',
    name: '友人帐',
    component: Friends
  },
  {
    path: '/resource',
    name: '资源分享',
    component: Resource
  },
  {
    path: '/thing',
    name: '点点滴滴',
    component: Thing
  },
  {
    path: '/about',
    name: '关于本站',
    component: About
  },
  {
    path: '/msg',
    name: '留言板',
    component: Message
  },
  {
    path: '/music',
    name: '音乐盒',
    component: Music,
    redirect: '/music/find_music',
    children: [{
      path: '/music/find_music',
      name: '发现音乐',
      component: FindMusic
    }, {
      path: '/music/search',
      name: '搜索歌曲列表',
      component: Search
    }, {
      path: '/music/song_list/:id',
      name: '歌单详情列表页',
      component: SongList
    }]
  },
  {
    path: '/admin',
    name: '后台首页',
    component: Admin,
    redirect: '/admin/index',
    children: [{
      path: '/admin/users',
      name: '用户列表',
      component: Users
    }, {
      path: '/admin/information',
      name: '个人信息',
      component: Information
    }, {
      path: '/admin/index',
      name: '后台首页',
      component: AdminIndex
    }, {
      path: '/admin/tags',
      name: '标签',
      component: Tag
    }, {
      path: '/admin/category',
      name: '分类',
      component: Category
    }, {
      path: '/admin/share',
      name: '友情链接',
      component: Share
    }, {
      path: '/admin/resource',
      name: '资源列表',
      component: Resources
    }, {
      path: '/admin/keep_diarys',
      name: '记录日记',
      component: Keep
    }, {
      path: '/admin/diarys',
      name: '日记列表',
      component: Diarys
    }, {
      path: '/admin/publish',
      name: '发表文章',
      component: Write
    }, {
      path: '/admin/articles',
      name: '文章列表',
      component: Article
    }, {
      path: '/admin/login_log',
      name: '登录日志',
      component: LoginLog
    }, {
      path: '/admin/log',
      name: '操作日志',
      component: Log
    }, {
      path: '/admin/comments',
      name: '评论列表',
      component: Comment
    }, {
      path: '/admin/messages',
      name: '留言列表',
      component: Msg
    }]
  }
]

const router = new VueRouter({
  mode: "history",
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  if (!to.path.includes('/admin')) {
    return next();
  } else {
    // 获取token
    const tokenStr = window.sessionStorage.getItem('token')
    if (!tokenStr) return next('/login')
  }
  next()
})

router.afterEach((to, from, next) => {
  document.querySelector('body').setAttribute('style', 'overflow: auto !important;')
})

export default router