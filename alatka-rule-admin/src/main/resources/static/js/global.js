$("#dataTable").bootstrapTable({
    // 远程数据加载失败时
    onLoadError: function (status) {
        console.log("调用失败, http code: " + status)
    },

    responseHandler: function (res) {
        console.log("===================222 " + res.data)
        if (res.code !== "0000") {
            return;
        }
        if (res.totalRecords === undefined) {
            return res.data;
        }
        return {
            total: res.totalRecords,
            rows: res.data
        };
    },
    // 远程数据加载成功时
    onLoadSuccess: function (data) {
        console.log("===================111 " + data)
    }
})