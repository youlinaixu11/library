import request from "@/utils/request";

export default (await import('vue')).defineComponent({
name: 'HomeView',
data() {
return {
tableData: []
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
res.data.list;
});
}
}
});
