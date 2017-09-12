webpackJsonp([35],{1316:function(e,a){e.exports={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("div",{staticClass:"components-container"},[t("el-form",{attrs:{inline:!0}},[t("el-form-item",[t("router-link",{attrs:{to:"/terminal/list"}},[t("el-button",{attrs:{type:"text"}},[t("i",{staticClass:"el-icon-arrow-left"}),e._v("返回\n\t            ")])],1)],1),e._v(" "),t("el-form-item",{staticClass:"fr"},[t("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(a){e.dialogAddPackageVisible=!0}}},[t("i",{staticClass:"el-icon-plus"}),e._v("添加升级包")])],1)],1),e._v(" "),t("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.loading,expression:"loading",modifiers:{body:!0}}],staticStyle:{width:"100%"},attrs:{data:e.list,"highlight-current-row":e.highlight,"element-loading-text":"拼命加载中"}},[t("el-table-column",{attrs:{type:"index"}}),e._v(" "),t("el-table-column",{attrs:{prop:"package_id",label:"升级包ID"}}),e._v(" "),t("el-table-column",{attrs:{prop:"package_terminalId",label:"终端ID"}}),e._v(" "),t("el-table-column",{attrs:{prop:"package_name",label:"升级包名称"}}),e._v(" "),t("el-table-column",{attrs:{prop:"package_introduce",label:"描述"}}),e._v(" "),t("el-table-column",{attrs:{label:"操作",width:"300"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"primary",size:"small",icon:"edit"},on:{click:function(t){e.showEdit(a.row)}}},[e._v("编辑")]),e._v(" "),t("el-button",{attrs:{type:"primary",size:"small",icon:"delete"},on:{click:function(t){e.del(a.row)}}},[e._v("删除")]),e._v(" "),t("el-button",{attrs:{type:"primary",size:"small",icon:"information"},on:{click:function(t){e.toVersion(a.row)}}},[e._v("版本管理")])]}}])})],1),e._v(" "),t("el-dialog",{attrs:{title:"添加升级包",visible:e.dialogAddPackageVisible,"lock-scroll":!0,size:"tiny"},on:{"update:visible":function(a){e.dialogAddPackageVisible=a},close:function(a){e.resetForm("packageForm")}}},[t("el-form",{ref:"packageForm",attrs:{model:e.packageForm,rules:e.rules,"label-width":"70px"}},[t("el-form-item",{attrs:{label:"包名称",prop:"package_name"}},[t("el-input",{attrs:{maxlength:16,placeholder:"包名称"},model:{value:e.packageForm.package_name,callback:function(a){e.packageForm.package_name=a},expression:"packageForm.package_name"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"描述",prop:"introduce"}},[t("el-input",{attrs:{type:"textarea",maxlength:70,placeholder:"描述"},model:{value:e.packageForm.introduce,callback:function(a){e.packageForm.introduce=a},expression:"packageForm.introduce"}})],1)],1),e._v(" "),t("div",{staticClass:"dialog-footer",slot:"footer"},[t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.addPackage()}}},[e._v("添加")]),e._v(" "),t("el-button",{on:{click:function(a){e.dialogAddPackageVisible=!1}}},[e._v("取消")])],1)],1),e._v(" "),t("el-dialog",{attrs:{title:"更新升级包",visible:e.dialogUpdatePackageVisible,"lock-scroll":!0,size:"tiny"},on:{"update:visible":function(a){e.dialogUpdatePackageVisible=a},close:function(a){e.resetForm("updatePackageForm")}}},[t("el-form",{ref:"updatePackageForm",attrs:{model:e.updatePackageForm,rules:e.rules,"label-width":"70px"}},[t("el-form-item",{attrs:{label:"包名称",prop:"package_name"}},[t("el-input",{attrs:{maxlength:16,placeholder:"包名称"},model:{value:e.updatePackageForm.package_name,callback:function(a){e.updatePackageForm.package_name=a},expression:"updatePackageForm.package_name"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"描述",prop:"introduce"}},[t("el-input",{attrs:{type:"textarea",maxlength:70,placeholder:"描述"},model:{value:e.updatePackageForm.introduce,callback:function(a){e.updatePackageForm.introduce=a},expression:"updatePackageForm.introduce"}})],1)],1),e._v(" "),t("div",{staticClass:"dialog-footer",slot:"footer"},[t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.updatePackage()}}},[e._v("确定")]),e._v(" "),t("el-button",{on:{click:function(a){e.dialogUpdatePackageVisible=!1}}},[e._v("取消")])],1)],1)],1)},staticRenderFns:[]}},621:function(e,a,t){var r=t(21)(t(885),t(1316),null,null,null);e.exports=r.exports},651:function(e,a,t){"use strict";function r(e){return t.i(h.a)({url:"/ong/terminal?cmd=add_terminal",method:"post",data:e})}function i(e){return t.i(h.a)({url:"/ong/terminal?cmd=update_terminal_info",method:"post",data:e})}function n(e,a,r,i){var n={userId:e,terminal_id:a,cur_userId:r,competence_type:i};return t.i(h.a)({url:"/ong/terminal?cmd=update_terminal_user_competence",method:"post",data:n})}function o(e){return t.i(h.a)({url:"/ong/terminal?cmd=get_terminals",method:"get",params:e})}function c(e,a){var r={userId:e,terminal_id:a};return t.i(h.a)({url:"/ong/terminal?cmd=delete_terminals",method:"delete",data:r})}function l(e,a,r,i){var n={cur_userId:e,terminal_id:a,userId:r,competence_type:i};return t.i(h.a)({url:"/ong/terminal?cmd=add_terminal_user",method:"post",data:n})}function s(e){return t.i(h.a)({url:"/ong/terminal?cmd=get_user_terminal_competence",method:"get",params:{terminal_id:e}})}function d(e){var a=e;return t.i(h.a)({url:"/ong/package?cmd=add_package",method:"post",data:a})}function m(e){var a=e;return t.i(h.a)({url:"/ong/package?cmd=update_package",method:"post",data:a})}function u(e){return t.i(h.a)({url:"/ong/package?cmd=get_packages",method:"get",params:e})}function g(e){return t.i(h.a)({url:"/ong/package?cmd=delete_package",method:"delete",data:e})}function p(e){return t.i(h.a)({url:"/ong/version?cmd=add_version",method:"post",data:e})}function k(e){return t.i(h.a)({url:"/ong/version?cmd=update_package_version",method:"post",data:e})}function f(e,a){var r={userId:e,package_id:a};return t.i(h.a)({url:"/ong/version?cmd=get_package_version_info",method:"get",params:r})}function _(e,a){var r={userId:e,version_id:a};return t.i(h.a)({url:"/ong/version?cmd=delete_package_version",method:"delete",data:r})}a.o=r,a.n=i,a.f=n,a.l=o,a.m=c,a.g=l,a.e=s,a.i=d,a.j=m,a.h=u,a.k=g,a.a=p,a.c=k,a.d=f,a.b=_;var h=t(230),v=t(97);t.n(v)},885:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0}),function(e){var r=t(651),i=t(70),n=t(92);t.n(n);a.default={methods:{fetchData:function(){var e=this;this.loading=!0;var a=this.params;t.i(r.h)(a).then(function(a){e.loading=!1;var t=a.data;"0000"===t.code?e.list=t.packageArray:e.$message(t.message)}).catch(function(a){e.loading=!0,e.$message({message:"服务器出错了！",type:"error"})})},addPackage:function(){var a=this;this.$refs.packageForm.validate(function(n){if(n){a.dialogAddPackageVisible=!1;var o=e.extend(t.i(i.a)(a.params),a.packageForm);t.i(r.i)(o).then(function(e){var t=e.data;"0000"==t.code?(a.$message.success("添加升级包成功！"),a.fetchData()):a.$message.warning(t.message)}).catch(function(e){a.$message.error(e.message?e.message:"服务器出错了！")})}})},showEdit:function(e){this.dialogUpdatePackageVisible=!0,this.updatePackageForm.package_name=e.package_name,this.updatePackageForm.package_id=e.package_id,this.updatePackageForm.introduce=e.package_introduce,this.updatePackageForm.terminal_id=e.package_terminalId},updatePackage:function(){var a=this;this.$refs.updatePackageForm.validate(function(n){if(n){a.dialogUpdatePackageVisible=!1;var o=e.extend(t.i(i.a)(a.params),a.updatePackageForm);t.i(r.j)(o).then(function(e){var t=e.data;"0000"==t.code?(a.$message.success("更新成功！"),a.fetchData()):a.$message.warning(t.message)}).catch(function(e){a.$message.error(e.message?e.message:"服务器出错了！")})}})},del:function(a){var o=this;this.$confirm("确定要删除:"+a.package_name+"?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(c){var l=n.Loading.service({target:e(".components-container")[0],text:"删除中..."}),s=e.extend(t.i(i.a)(o.params),a);t.i(r.k)(s).then(function(e){var a=e.data;l.close(),"0000"==a.code?(o.$message.success("删除成功！"),o.fetchData()):o.$message.warning(a.message)}).catch(function(e){l.close(),o.$message.error(e.message?e.message:"服务器出错了！")})}).catch(function(e){})},resetForm:function(e){this.$refs[e].resetFields()},toVersion:function(e){var a=e.package_id,t=this.params.terminal_id,r="/terminal/"+t+"/package/"+a+"/versionList";this.$router.push({path:r})}},data:function(){return{list:[],loading:!1,highlight:!0,dialogAddPackageVisible:!1,dialogUpdatePackageVisible:!1,params:{userId:this.$store.getters.userId,terminal_id:this.$route.params.terminalId},packageForm:{package_name:"",introduce:""},updatePackageForm:{package_name:"",introduce:"",package_id:"",terminal_id:""},rules:{package_name:[{required:!0,message:"名称必填",trigger:"blur"},{max:16,message:"名称长度不能超过16",trigger:"blur"}],introduce:[{max:74,message:"描述太长",trigger:"blur"}]}}},created:function(){this.fetchData()}}}.call(a,t(151))}});
//# sourceMappingURL=35.864f4ebe42d40d04273f.js.map