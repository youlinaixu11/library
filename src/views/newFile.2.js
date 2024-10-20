import request from "@/utils/request";

export default (await import('vue')).defineComponent({
name: 'HomeView',
data() {
return {
tableData: [],
total: 0
};
},
created() {
this.load();
},
methods: {
load() {
// fetch('http://localhost:9090/user/list').then(res => res.json()).then(res => {
//   console.log(res)
//   this.tableData = res
// })
request.get('/user/page').then(res => {
if (res.code === '200') {
this.tableData = res.data.list;
this.total = res.data.total;
}
});
}
}
});
