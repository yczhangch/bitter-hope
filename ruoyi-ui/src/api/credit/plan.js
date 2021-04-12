import request from '@/utils/request'

// 查询信用卡借款计划列表
export function listPlan(query) {
  return request({
    url: '/credit/plan/list',
    method: 'get',
    params: query
  })
}

// 查询信用卡借款计划详细
export function getPlan(id) {
  return request({
    url: '/credit/plan/' + id,
    method: 'get'
  })
}

// 新增信用卡借款计划
export function addPlan(data) {
  return request({
    url: '/credit/plan',
    method: 'post',
    data: data
  })
}

// 修改信用卡借款计划
export function updatePlan(data) {
  return request({
    url: '/credit/plan',
    method: 'put',
    data: data
  })
}

// 删除信用卡借款计划
export function delPlan(id) {
  return request({
    url: '/credit/plan/' + id,
    method: 'delete'
  })
}

// 导出信用卡借款计划
export function exportPlan(query) {
  return request({
    url: '/credit/plan/export',
    method: 'get',
    params: query
  })
}

// 银行卡列表
export function getCreditCardList() {
  return request({
    url: '/credit/card/creditCardList',
    method: 'get'
  })
}

// pos机列表
export function getPosList() {
  return request({
    url: '/credit/pos/posList',
    method: 'get'
  })
}
