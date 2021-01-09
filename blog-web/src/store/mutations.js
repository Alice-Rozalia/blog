export const setIsAuthenticate = (state, data) => {
  state.isAuthenticate = data
}

export const setUser = (state, data) => {
  state.user = data
  window.sessionStorage.setItem('user', JSON.stringify(data))
}

export const setToken = (state, token) => {
  state.token = token
  sessionStorage.token = token
}

export const delToken = (state) => {
  state.token = ''
  sessionStorage.removeItem('token')
}

export const setSearchList = (state, { songs, total }) => {
  state.searchList = songs
  state.searchTotal = total
}

export const setMusic = (state, music) => {
  state.music = music
}

export const setArticles = (state, data) => {
  state.articles = data
}

export const setTotal = (state, data) => {
  state.total = data
}