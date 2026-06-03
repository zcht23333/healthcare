import request from '@/utils/request'

export function getCityList(params) { return request({ url: '/city/list', method: 'get', params }) }
export function addCity(data) { return request({ url: '/city/add', method: 'post', data }) }
export function updateCity(data) { return request({ url: '/city/update', method: 'post', data }) }
export function deleteCity(id) { return request({ url: `/city/delete/${id}`, method: 'post' }) }