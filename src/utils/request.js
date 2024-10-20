import axios from "axios";
import Cookies from "js-cookie";
import router from "@/router";

//创建一个axios对象
const request= axios.create({
    baseURL:'http://localhost:9090/api',
    timeout:5000
})

//request拦截器
//可以自请求发送前对请求做一些处理
//比如统一加token，对请求参数统一加密
request.interceptors.request.use(config =>{
        config.headers['Content-Type'] = 'application/json;charset=utf-8';

         const adminJson = Cookies.get('admin')
         if (adminJson) {
             config.headers['token'] = JSON.parse(adminJson).token
         }

        return config},

    error =>{
        return Promise.reject(error)
    });

//response 拦截器
//可以在接口响应后统一处理一些结果
//response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        return res;
    },
    error => {
        if (error.response) {
            // 后端返回了HTTP响应，但状态码不是2xx
            if (error.response.status === 401 || error.response.status === 403) {
                // 处理认证失败的情况
                console.error("登录已过期或认证失败");
            } else {
                // 处理其他错误
                console.error("接口错误: " + error.response.data.msg);
            }
        } else if (error.request) {
            // 发送请求但未收到响应，可能服务器未响应
            console.error("服务器未响应");
        } else {
            // 设置请求时或请求选项时发生的错误
            console.error("请求错误: " + error.message);
        }
        return Promise.reject(error);
    }
);

export default request