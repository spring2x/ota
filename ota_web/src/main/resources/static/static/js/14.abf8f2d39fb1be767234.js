webpackJsonp([14],{1231:function(e,t,r){var a=r(944);"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);r(573)("6b837168",a,!0)},1321:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"components-container"},[r("el-form",{attrs:{inline:!0}},[r("el-form-item",[r("router-link",{attrs:{to:"/terminal/list"}},[r("el-button",{attrs:{type:"text"}},[r("i",{staticClass:"el-icon-arrow-left"}),e._v("返回\n          ")])],1)],1),e._v(" "),r("el-form-item",{staticClass:"fr"},[e.terminalOwnerId==e.loginUserId?r("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(t){e.dialogAddUserVisible=!0}}},[r("i",{staticClass:"el-icon-plus"}),e._v("添加使用用户")]):e._e()],1)],1),e._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.loading,expression:"loading",modifiers:{body:!0}}],staticStyle:{width:"100%"},attrs:{data:e.list,"row-class-name":e.tableRowClassName,"highlight-current-row":e.highlight,"element-loading-text":"拼命加载中"}},[r("el-table-column",{attrs:{type:"index",width:"50"}}),e._v(" "),r("el-table-column",{attrs:{prop:"userName",label:"用户名"}}),e._v(" "),r("el-table-column",{attrs:{prop:"competenceType",label:"权限",scope:"scope",formatter:e.handleType}}),e._v(" "),r("el-table-column",{attrs:{label:"操作",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{type:"primary",size:"small",disabled:e.terminalOwnerId!=e.loginUserId||0==t.row.competenceType},on:{click:function(r){e.showUpdateDailog(t.row)}}},[e._v("更新权限")])]}}])})],1),e._v(" "),r("el-dialog",{attrs:{title:"更新权限",visible:e.dialogFormVisible,"lock-scroll":!0,size:"tiny"},on:{"update:visible":function(t){e.dialogFormVisible=t},close:function(t){e.resetForm("termialForm")}}},[r("el-form",{ref:"termialForm",attrs:{"label-width":"80px"}},[r("el-form-item",{attrs:{label:"权限"}},[r("el-select",{attrs:{placeholder:"请选择"},model:{value:e.selectedUserCompetenceType,callback:function(t){e.selectedUserCompetenceType=t},expression:"selectedUserCompetenceType"}},e._l(e.compenenceArray,function(e){return r("el-option",{key:e.id,attrs:{label:e.name,value:e.type}})}))],1)],1),e._v(" "),r("div",{staticClass:"dialog-footer",slot:"footer"},[r("el-button",{attrs:{type:"primary"},on:{click:e.updateUserCompetence}},[e._v("确定")]),e._v(" "),r("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取消")])],1)],1),e._v(" "),r("el-dialog",{attrs:{title:"添加使用用户",visible:e.dialogAddUserVisible,"lock-scroll":!0,size:"tiny"},on:{"update:visible":function(t){e.dialogAddUserVisible=t},close:function(t){e.resetForm("userForm")}}},[r("el-form",{ref:"userForm",attrs:{model:e.userForm,rules:e.rules,"label-width":"120px"}},[r("el-form-item",{attrs:{label:"被添加的用户",prop:"userId"}},[r("el-select",{attrs:{placeholder:"请选择人员"},model:{value:e.userForm.userId,callback:function(t){e.userForm.userId=t},expression:"userForm.userId"}},e._l(e.userList,function(e){return r("el-option",{key:e.id,attrs:{value:e.id,label:e.uname}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"权限",prop:"competence_type"}},[r("el-select",{attrs:{placeholder:"请选择"},model:{value:e.userForm.competence_type,callback:function(t){e.userForm.competence_type=t},expression:"userForm.competence_type"}},e._l(e.compenenceArray,function(e){return r("el-option",{key:e.id,attrs:{label:e.name,value:e.type}})}))],1)],1),e._v(" "),r("div",{staticClass:"dialog-footer",slot:"footer"},[r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addUser()}}},[e._v("添加")]),e._v(" "),r("el-button",{on:{click:function(t){e.dialogAddUserVisible=!1}}},[e._v("取消")])],1)],1)],1)},staticRenderFns:[]}},622:function(e,t,r){function a(e){r(1231)}var n=r(21)(r(886),r(1321),a,"data-v-b85f1424",null);e.exports=n.exports},651:function(e,t,r){"use strict";function a(e){return r.i(_.a)({url:"/ong/terminal?cmd=add_terminal",method:"post",data:e})}function n(e){return r.i(_.a)({url:"/ong/terminal?cmd=update_terminal_info",method:"post",data:e})}function o(e,t,a,n){var o={userId:e,terminal_id:t,cur_userId:a,competence_type:n};return r.i(_.a)({url:"/ong/terminal?cmd=update_terminal_user_competence",method:"post",data:o})}function s(e){return r.i(_.a)({url:"/ong/terminal?cmd=get_terminals",method:"get",params:e})}function i(e,t){var a={userId:e,terminal_id:t};return r.i(_.a)({url:"/ong/terminal?cmd=delete_terminals",method:"delete",data:a})}function l(e,t,a,n){var o={cur_userId:e,terminal_id:t,userId:a,competence_type:n};return r.i(_.a)({url:"/ong/terminal?cmd=add_terminal_user",method:"post",data:o})}function c(e){return r.i(_.a)({url:"/ong/terminal?cmd=get_user_terminal_competence",method:"get",params:{terminal_id:e}})}function d(e){var t=e;return r.i(_.a)({url:"/ong/package?cmd=add_package",method:"post",data:t})}function u(e){var t=e;return r.i(_.a)({url:"/ong/package?cmd=update_package",method:"post",data:t})}function m(e){return r.i(_.a)({url:"/ong/package?cmd=get_packages",method:"get",params:e})}function p(e){return r.i(_.a)({url:"/ong/package?cmd=delete_package",method:"delete",data:e})}function f(e){return r.i(_.a)({url:"/ong/version?cmd=add_version",method:"post",data:e})}function g(e){return r.i(_.a)({url:"/ong/version?cmd=update_package_version",method:"post",data:e})}function v(e,t){var a={userId:e,package_id:t};return r.i(_.a)({url:"/ong/version?cmd=get_package_version_info",method:"get",params:a})}function h(e,t){var a={userId:e,version_id:t};return r.i(_.a)({url:"/ong/version?cmd=delete_package_version",method:"delete",data:a})}t.o=a,t.n=n,t.f=o,t.l=s,t.m=i,t.g=l,t.e=c,t.i=d,t.j=u,t.h=m,t.k=p,t.a=f,t.c=g,t.d=v,t.b=h;var _=r(230),b=r(97);r.n(b)},716:function(e,t,r){"use strict";function a(){return r.i(n.a)({url:"/ong/user?cmd=get_users",method:"get"})}t.a=a;var n=r(230)},717:function(e,t,r){"use strict";r.d(t,"a",function(){return a});var a=[{id:1,introduce:"超级管理员权限，对终端具有所有增删改查权限",name:"超级管理员",type:0},{id:2,introduce:"管理员权限，对终端只具有增加、修改、查看权限",name:"管理员",type:1},{id:3,introduce:"普通权限，对终端只有查看权限",name:"普通用户",type:2}]},886:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=r(651),n=r(716),o=r(717);t.default={methods:{handleType:function(e,t,r){for(var a=e.competenceType,n=0;n<o.a.length;n++)if(a==o.a[n].type)return o.a[n].name},tableRowClassName:function(e,t){return 1===t?"info-row":3===t?"positive-row":""},fetchUserList:function(){var e=this;r.i(n.a)().then(function(t){var r=t.data;"0000"===r.code?e.userList=r.users.userArray:e.$message(r.message)}).catch(function(t){e.$message({message:"服务器出错了！",type:"error"})})},fetchData:function(){var e=this;this.loading=!0,r.i(a.e)(this.$route.params.terminalId).then(function(t){e.loading=!1;var r=t.data;"0000"===r.code?(e.list=r.terminals.terminalArray,e.list.forEach(function(t){console.log(t),0==t.competenceType&&(e.terminalOwnerId=t.userId)})):e.$message.warning(r.message)}).catch(function(t){e.loading=!0,e.$message({message:"服务器出错了！",type:"error"})})},resetForm:function(e){this.$refs[e].resetFields()},showUpdateDailog:function(e){this.dialogFormVisible=!0,this.selectedUserCompetenceType=e.competenceType,this.selectedUserId=e.userId},updateUserCompetence:function(){var e=this,t=this.$store.getters.userId,n=this.$route.params.terminalId;r.i(a.f)(this.selectedUserId,n,t,this.selectedUserCompetenceType).then(function(t){var r=t.data;"0000"==r.code?(e.$message.success("操作成功！"),e.fetchData()):e.$message.warning(r.message),e.dialogFormVisible=!1}).catch(function(t){e.$message.error(t.message)})},addUser:function(){var e=this;this.$refs.userForm.validate(function(t){if(t){e.dialogAddUserVisible=!1;var n=e.$store.getters.userId,o=e.$route.params.terminalId;r.i(a.g)(n,o,e.userForm.userId,e.userForm.competence_type).then(function(t){var r=t.data;"0000"==r.code?(e.$message.success("添加成功"),e.fetchData()):e.$message.warning(r.message)}).catch(function(t){e.$message.error(t.message?t.message:"服务器出错了！")}),e.$refs.userForm.resetFields()}})}},data:function(){return{list:[],userList:[],loading:!1,highlight:!0,dialogFormVisible:!1,dialogAddUserVisible:!1,selectedUserCompetenceType:"",selectedUserId:"",compenenceArray:o.a,loginUserId:this.$store.getters.userId,terminalOwnerId:"",userForm:{userId:"",competence_type:""},rules:{userId:[{required:!0,type:"number",message:"需要被添加的用户id必填",trigger:"blur"}],competence_type:[{required:!0,type:"number",message:"添加用户的终端权限必填",trigger:"change"}]}}},created:function(){this.fetchData(),this.fetchUserList()},watch:{$route:"fetchData"}}},944:function(e,t,r){t=e.exports=r(572)(!0),t.push([e.i,".el-table .info-row[data-v-b85f1424]{background:#c9e5f5}.el-table .positive-row[data-v-b85f1424]{background:#e2f0e4}","",{version:3,sources:["E:/dev/study/admin/src/views/terminal/terminalUserList.vue"],names:[],mappings:"AACA,qCACE,kBAAoB,CACrB,AACD,yCACE,kBAAoB,CACrB",file:"terminalUserList.vue",sourcesContent:["\n.el-table .info-row[data-v-b85f1424] {\n  background: #c9e5f5;\n}\n.el-table .positive-row[data-v-b85f1424] {\n  background: #e2f0e4;\n}\n"],sourceRoot:""}])}});
//# sourceMappingURL=14.abf8f2d39fb1be767234.js.map