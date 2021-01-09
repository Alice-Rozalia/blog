import {
  music
} from '@/utils/request'

// 推荐歌单
export function Personalized() {
  return music({
    url: '/personalized?limit=12',
    method: 'get'
  })
}

// 推荐新音乐
export function newSong() {
  return music({
    url: '/personalized/newsong',
    method: 'get'
  })
}

// 推荐节目 (个性推荐)
export function Recommend() {
  return music({
    url: '/program/recommend',
    method: 'get'
  })
}

// 独家放送
export function Privatecontent() {
  return music({
    url: '/personalized/privatecontent',
    method: 'get'
  })
}

// 推荐MV
export function personalizedMV() {
  return music({
    url: '/personalized/mv',
    method: 'get'
  })
}

// 电台-热门电台榜
export function Radio() {
  return music({
    url: '/dj/toplist?type=hot&limit=6',
    method: 'get'
  })
}