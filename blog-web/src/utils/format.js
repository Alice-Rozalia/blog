import Vue from 'vue'

Vue.filter('dateFormat', function (originVal) {
  const dt = new Date(originVal)

  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDate() + '').padStart(2, '0')

  const hh = (dt.getHours() + '').padStart(2, '0')
  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')

  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})

Vue.filter('dateFormat2', function (originVal) {
  const dt = new Date(originVal)

  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDate() + '').padStart(2, '0')

  return `${y}-${m}-${d}`
})

Vue.filter('durationFormat', function (originVal) {
  const dt = new Date(originVal)

  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')

  return `${mm}:${ss}`
})

Vue.filter('million', function (value) {
  let num;
  if (value > 9999) {
    num = (Math.floor(value / 1000) / 10) + ' 万';
  } else if (value < 9999 && value > -9999) {
    num = value
  } else if (value < -9999) {
    num = -(Math.floor(Math.abs(value) / 1000) / 10) + ' 万'
  }
  return num;
})