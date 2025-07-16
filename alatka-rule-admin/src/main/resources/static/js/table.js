$("#dataTable").bootstrapTable({
    // 远程数据加载失败时
    onLoadError: function (status) {
        console.log("调用失败, http code: " + status)
    },

    queryParams: function (params) {
        const formData = new FormData($('#searchForm')[0]);
        const searchData = {};
        formData.forEach((value, key) => {
            if (searchData.hasOwnProperty(key)) {
                if (!Array.isArray(searchData[key])) {
                    searchData[key] = [searchData[key]];
                }
                searchData[key].push(value);
            } else if (value) {
                searchData[key] = value;
            }
        });

        return {
            pageNo: params.offset / params.limit + 1,
            pageSize: params.limit,
            orderBy: params.sort,
            direction: params.order,
            ...searchData
        }
    },

    responseHandler: function (res) {
        if (res.code !== "0000") {
            console.log("接口错误, " + res.msg);
            return;
        }
        return {
            total: res.totalRecords,
            rows: res.data
        };
    }
})

$("#resetButton").click(function () {
    $("#searchForm")[0].reset();
})

$("#searchButton").click(function () {
    refresh();
})

function showAddModal(url) {
    $('#addForm')[0]?.reset();
    $('#addModal').modal('show');

    $('#saveAddBtn').off('click').on('click', function () {
        const formData = {};
        $('#addForm').serializeArray().forEach(item => {
            formData[item.name] = item.value;
        });
        submitFunction(url, 'POST', formData, '新增');
        $('#addModal').modal('hide');
    });
}

function showEditModal(url) {
    const selection = $('#dataTable').bootstrapTable('getSelections');
    if (selection.length !== 1) {
        showWarningToast("请选择一条记录进行编辑");
        return;
    }

    const row = selection[0];
    $('#editId').val(row.id);
    $('#editKey').val(row.key);
    $('#editName').val(row.name);
    $('#editType').val(row.type);
    $('#editEnabled').val(row.enabled ? 'true' : 'false');

    $('#editModal').modal('show');

    $('#saveEditBtn').off('click').on('click', function () {
        const formData = {};
        $('#editForm').serializeArray().forEach(item => {
            formData[item.name] = item.value;
        });
        submitFunction(url, 'PUT', formData, '更新');
        $('#editModal').modal('hide');
    });
}

function showDeleteModal(url) {
    const selections = $('#dataTable').bootstrapTable('getSelections');
    if (selections.length !== 1) {
        showWarningToast("请选择一条记录");
        return;
    }

    $('#deleteModal').modal('show');
    $('#saveDeleteBtn').off('click').on('click', function () {
        const ids = selections.map(row => row.id);
        submitFunction(url + '?id=' + ids[0], 'DELETE', null, '删除');
        $('#deleteModal').modal('hide');
    });
}

function submitFunction(url, methodType, data, actionName) {
    $.ajax({
        url: url,
        type: methodType,
        contentType: 'application/json',
        data: data && JSON.stringify(data),
        success: function (response) {
            if (response.code === "0000") {
                showSuccessToast(actionName + "成功");
                refresh();
            } else {
                showErrorToast(actionName + "失败: " + response.msg);
            }
        },
        error: function (xhr) {
            showErrorToast(actionName + "请求失败: " + xhr.responseJSON?.message || "未知错误");
        }
    });
}

function refresh() {
    $('#dataTable').bootstrapTable('refreshOptions', {
        pageNumber: 1,
        sortOrder: "desc",
        sortName: "id"
    });
}

function showSuccessToast(message) {
    showToast(message, 'bg-success');
}

function showErrorToast(message) {
    showToast(message, 'bg-danger');
}

function showWarningToast(message) {
    showToast(message, 'bg-warning');
}

function showToast(message, bgClass) {
    const toastHtml = `
        <div class="toast align-items-center text-white ${bgClass} border-0 position-fixed top-20 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    $(toastHtml).appendTo('body').toast({autohide: true, delay: 3000}).toast('show');

    // 自动移除
    setTimeout(() => {
        $('.toast').remove();
    }, 3000);
}

function initRuleGroupSelect() {
    $.ajax({
        url: '/rule/group/map',
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            if (response.code === "0000") {
                const map = new Map(Object.entries(response.data));
                $('#groupKey').empty();
                map.forEach((value, key) => {
                    $('#groupKey').append($('<option>', {
                        value: key,
                        text: value
                    }))
                });

            } else {
                showErrorToast("失败: " + response.msg);
            }
        },
        error: function (xhr) {
            showErrorToast("请求失败: " + xhr.responseJSON?.message || "未知错误");
        }
    });
}

function enabledFormatter(arg) {
    if (arg === true) {
        return "正常";
    }
    if (arg === false) {
        return "禁用";
    }
    return "/";
}