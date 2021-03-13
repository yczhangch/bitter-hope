import request from '@/utils/request'

// 查询信用卡还款日提醒列表
export function listPayback(query) {
  return request({
    url: '/credit/payback/list',
    method: 'get',
    params: query
  })
}

// 查询信用卡还款日提醒详细
export function getPayback(id) {
  return request({
    url: '/credit/payback/' + id,
    method: 'get'
  })
}

// 新增信用卡还款日提醒
export function addPayback(data) {
  return request({
    url: '/credit/payback',
    method: 'post',
    data: data
  })
}

// 修改信用卡还款日提醒
export function updatePayback(data) {
  return request({
    url: '/credit/payback',
    method: 'put',
    data: data
  })
}

// 删除信用卡还款日提醒
export function delPayback(id) {
  return request({
    url: '/credit/payback/' + id,
    method: 'delete'
  })
}

// 导出信用卡还款日提醒
export function exportPayback(query) {
  return request({
    url: '/credit/payback/export',
    method: 'get',
    params: query
  })
}

// 是否还款状态修改
export function isPayed(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/credit/payback',
    method: 'put',
    data: data
  })
}
