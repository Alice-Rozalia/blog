import {
  music
} from '@/utils/request'

// 获取歌单详情
export function playList(id) {
  return music({
    url: '/playlist/detail',
    method: 'get',
    params: {
      id: id
    }
  })
}

// 检验用于是否可用
export function check(id) {
  return music({
    url: '/check/music',
    method: 'get',
    params: {
      id: id
    }
  })
}

// 获取音乐的url
export function song(id) {
  return music({
    url: '/song/url',
    method: 'get',
    params: {
      id: id
    }
  })
}

// 获取歌词
export function lyric(id) {
  return music({
    url: '/lyric',
    method: 'get',
    params: {
      id: id
    }
  })
}

// 获取歌曲详情
export function songDetail(id) {
  return music({
    url: '/song/detail',
    method: 'get',
    params: {
      ids: id
    }
  })
}