function initTable() {
    $("#dataTable").bootstrapTable({
        // 远程数据加载失败时
        onLoadError: function (status) {
            console.log("接口请求失败, http code: " + status)
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
                showErrorToast("接口响应失败: " + res.msg);
                return {
                    total: 0,
                    rows: []
                };
            }
            return {
                total: res.totalRecords,
                rows: res.data
            };
        }
    })
}

$("#resetButton").click(function () {
    $("#searchForm")[0].reset();
})

$("#searchButton").click(function () {
    refresh();
})

function showEditModal(url, created) {
    if (created) {
        $('#editForm')[0]?.reset();
    } else {
        const selection = $('#dataTable').bootstrapTable('getSelections');
        if (selection.length !== 1) {
            showWarningToast("请选择一条记录进行编辑");
            return;
        }

        const row = selection[0];
        Object.keys(row).forEach(field => {
            const $input = $(`#editForm [name="${field}"], #editForm #${field}`);
            if ($input.length) {
                let value = row[field];
                if (typeof value === 'boolean') {
                    value = value ? 'true' : 'false';
                }
                $input.val(value);
            }
        });
    }

    $('#editModal').modal('show');
    $('#saveEditBtn').off('click').on('click', function (event) {
        const formData = {};
        const $editForm = $('#editForm');
        if (!$editForm[0].checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
            $editForm.addClass('was-validated');
        } else {
            $editForm.serializeArray().forEach(item => {
                formData[item.name] = item.value;
            });
            submitFunction(url, created ? 'POST' : 'PUT', formData, created ? '新增' : '更新');
            $('#editModal').modal('hide');
        }
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
    httpClient(url, methodType, data, function (data) {
        showSuccessToast(actionName + "成功");
        refresh();
    });
}

function httpClient(url, methodType, data, success, error = function () {
}) {
    $.ajax({
        url: url,
        type: methodType,
        contentType: 'application/json',
        data: data && JSON.stringify(data),
        success: function (response) {
            if (response.code === "0000") {
                success(response.data);
            } else {
                showErrorToast("接口响应失败: " + response.msg);
                error();
            }
        },
        error: function (xhr) {
            showErrorToast("接口请求失败: " + xhr.responseJSON?.message || "未知错误");
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
    $('.toast').remove();

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

    const $toast = $(toastHtml).appendTo('body');

    // 初始化并显示 toast
    $toast.toast({
        autohide: true,
        delay: 3000
    }).toast('show');

    // 自动移除 toast 当隐藏时
    $toast.on('hidden.bs.toast', function () {
        $(this).remove();
    });
}

function initExtended() {
    $('#addExtendedBtn').click(function () {
        const property =
            `
         <div class="extended-property row g-2 mb-2">
             <div class="col-md-4">
                 <label>
                     <input type="text" class="form-control" name="extendedKey" placeholder="键"
                            required>
                 </label>
             </div>
             <div class="col-md-6">
                 <label>
                     <input type="text" class="form-control" name="extendedValue" placeholder="值"
                            required>
                 </label>
             </div>
             <div class="col-md-2 text-end">
                 <button type="button" class="btn btn-sm btn-danger remove-extended">
                     <i class="bi bi-trash"></i> 删除
                 </button>
             </div>
         </div>
       `;
        $('#extendedPropertiesContainer').append(property);
    });

    $('#extendedPropertiesContainer').on('click', '.remove-extended', function () {
        $(this).closest('.extended-property').remove();
    });
}

function initRuleGroupSelect() {
    httpClient('/rule/group/map', 'GET', null, function (data) {
        const map = new Map(Object.entries(data));
        map.forEach((value, key) => {
            $('#groupKey').append($('<option>', {value: key, text: value}))
            $('#editGroupKey').append($('<option>', {value: key, text: value}))
        });
    });
}

function groupKeyFormatter(arg) {
    const keyValuePairs = {};
    $('select#groupKey option').each(function () {
        keyValuePairs[$(this).val()] = $(this).text();
    });
    return keyValuePairs[arg];
}

function detailFormatter(index, row) {
    const map = new Map(Object.entries(row.extended));
    const html = [];
    html.push('<dl class="row">');
    map.forEach((value, key) => {
        html.push('<dt class="col-sm-2 text-end">' + key + ': </dt><dd class="col-sm-10">' + value + '</dd>');
    })
    html.push('</dl>');
    return html.join('');
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