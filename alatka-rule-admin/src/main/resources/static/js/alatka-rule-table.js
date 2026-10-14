function initTable() {
    $("#resetButton").off('click').on('click', function () {
        const $searchForm = $("#searchForm");
        $searchForm[0].reset();
        $searchForm.find('select').each(function () {
            if (this.tomselect) {
                this.tomselect.clear();
                const initValue = $(this).data('alk-init-value');
                if (initValue) {
                    this.tomselect.setValue(initValue);
                }
            }
        });
    });

    $("#searchButton").click(function () {
        refresh();
    })

    interact('.modal-dialog').draggable({
        allowFrom: '.modal-header',
        modifiers: [
            interact.modifiers.restrictRect({
                restriction: 'parent'
            })
        ],
        listeners: {
            move: function (event) {
                const target = event.target;
                const x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
                const y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

                target.style.transform = `translate(${x}px, ${y}px)`;
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);
            }
        }
    });

    $("#dataTable").bootstrapTable({
        undefinedText: '',
        pageList: [10, 25, 50, 100],
        onLoadError: function (status) {
            showErrorToast(`接口请求失败, http code: ${status}`);
        },
        iconSize: 'sm',
        buttonsClass: 'sm',
        buttons: buttonsFunction,
        buttonsOrder: buttonsOrderFunction(),

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

            $('#searchForm').find('select').each(function () {
                if (this.tomselect) {
                    const fieldName = $(this).attr('name');
                    const tsVal = this.tomselect.getValue();

                    if (fieldName) {
                        if (Array.isArray(tsVal)) {
                            if (tsVal.length) searchData[fieldName] = tsVal;
                            else delete searchData[fieldName];
                        } else if (tsVal !== null && tsVal !== undefined && tsVal !== '') {
                            searchData[fieldName] = tsVal;
                        } else {
                            delete searchData[fieldName];
                        }
                    }
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
                showErrorToast(`接口响应失败: ${res.msg}`);
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

function buttonsFunction() {
    return {};
}

function buttonsOrderFunction() {
    return ['addBtn', 'editBtn', 'deleteBtn', 'columns'];
}

function showEditModal(url, created) {
    const $form = $('#editForm');
    const $modal = $('#editModal');
    const formEl = $form[0];
    if (created) {
        formEl?.reset();
        $form.find('input[type="hidden"]').val('');
        $form.find('select').each(function () {
            if (!this.tomselect) return;
            this.tomselect.clear();

            const initValue = $(this).data('alk-init-value');
            if (initValue !== undefined && initValue !== null && initValue !== '') {
                this.tomselect.setValue(initValue);
            }
        });
    } else {
        const selection = $('#dataTable').bootstrapTable('getSelections');
        if (selection.length !== 1) {
            showWarningToast("请选择一条记录进行编辑");
            return;
        }

        const row = selection[0];
        Object.keys(row).forEach(field => {
            const $field = $form.find(`[name="${field}"]`);
            if (!$field.length) return;

            let value = row[field];

            if (typeof value === 'boolean') {
                value = value ? 'true' : 'false';
            }
            if (value === null || value === undefined) {
                value = '';
            }
            if ($field[0].tomselect) {
                $field[0].tomselect.setValue(value);
            } else {
                $field.val(value);
            }
        });
    }
    $form.removeClass('was-validated');
    $form.find('select').each(function () {
        if (this.tomselect) {
            $(this.tomselect.wrapper).removeClass('is-invalid is-valid');
        }
    });

    $modal.modal('show');
    $('#saveEditBtn').off('click').on('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        if (!formEl.checkValidity()) {
            $form.addClass('was-validated');
            $form.find('select').each(function () {
                if (!this.tomselect) return;
                if (this.checkValidity()) {
                    $(this.tomselect.wrapper).removeClass('is-invalid').addClass('is-valid');
                } else {
                    $(this.tomselect.wrapper).removeClass('is-valid').addClass('is-invalid');
                }
            });
            return;
        }
        const formData = {};
        $form.serializeArray().forEach(item => {
            formData[item.name] = item.value === '' ? null : item.value;
        });
        submitFunction(url, created ? 'POST' : 'PUT', formData, created ? '新增' : '更新');
        $modal.modal('hide');
    });
}

function showEditModalWithExtendedProperties(url, created) {
    const $form = $('#editForm');
    const $modal = $('#editModal');
    const formEl = $form[0];
    const $extendedPropertiesContainer = $('#extendedPropertiesContainer');
    if (created) {
        formEl?.reset();
        $form.find('input[type="hidden"]').val('');
        $form.find('select').each(function () {
            if (!this.tomselect) return;
            this.tomselect.clear();

            const initValue = $(this).data('alk-init-value');
            if (initValue !== undefined && initValue !== null && initValue !== '') {
                this.tomselect.setValue(initValue);
            }
        });
        $extendedPropertiesContainer.empty();
    } else {
        const selection = $('#dataTable').bootstrapTable('getSelections');
        if (selection.length !== 1) {
            showWarningToast("请选择一条记录");
            return;
        }

        const row = selection[0];
        Object.keys(row).forEach(field => {
            const $field = $form.find(`[name="${field}"]`);
            if (!$field.length) return;

            let value = row[field];

            if (typeof value === 'boolean') {
                value = value ? 'true' : 'false';
            }
            if (value === null || value === undefined) {
                value = '';
            }
            if ($field[0].tomselect) {
                $field[0].tomselect.setValue(value);
            } else {
                $field.val(value);
            }
        });

        $extendedPropertiesContainer.empty();
        const map = new Map(Object.entries(row.extended));
        map.forEach((value, key) => buildExtendedProperties(key, value));
    }

    $form.removeClass('was-validated');
    $form.find('select').each(function () {
        if (this.tomselect) {
            $(this.tomselect.wrapper).removeClass('is-invalid is-valid');
        }
    });

    $modal.modal('show');
    $('#saveEditBtn').off('click').on('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        if (!formEl.checkValidity()) {
            $form.addClass('was-validated');
            $form.find('select').each(function () {
                if (!this.tomselect) return;
                if (this.checkValidity()) {
                    $(this.tomselect.wrapper).removeClass('is-invalid').addClass('is-valid');
                } else {
                    $(this.tomselect.wrapper).removeClass('is-valid').addClass('is-invalid');
                }
            });
            return;
        }
        const formData = {};
        $form.not($extendedPropertiesContainer.find(':input'))
            .serializeArray()
            .forEach(item => formData[item.name] = item.value === '' ? null : item.value);

        const keys = $extendedPropertiesContainer.find('input[name="extendedKey"]').map(function () {
            return $(this).val();
        }).get();
        const values = $extendedPropertiesContainer.find('input[name="extendedValue"]').map(function () {
            return $(this).val();
        }).get();
        const extended = {};
        keys.forEach((key, index) => {
            if (key && index < values.length) {
                extended[key] = values[index] === '' ? null : values[index];
            }
        });
        formData['extended'] = extended;

        submitFunction(url, created ? 'POST' : 'PUT', formData, created ? '新增' : '更新');
        $modal.modal('hide');
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
        submitFunction(`${url}?id=${ids[0]}`, 'DELETE', null, '删除');
        $('#deleteModal').modal('hide');
    });
}

function submitFunction(url, methodType, data, actionName) {
    httpClient(url, methodType, data, function (data) {
        showSuccessToast(`${actionName}成功`);
        refresh();
    });
}

function refresh() {
    $('#dataTable').bootstrapTable('refreshOptions', {
        pageNumber: 1,
        sortOrder: "desc",
        sortName: "id"
    });
}

function initExtendedProperties() {
    $('#addExtendedBtn').click(function () {
        buildExtendedProperties();
    });

    $('#extendedPropertiesContainer').on('click', '.remove-extended', function () {
        $(this).closest('.extended-property').remove();
    });
}

function buildExtendedProperties(key = '', value = '') {
    const property =
        `
         <div class="extended-property row g-2 mb-2">
             <div class="col-md-3">
                  <input type="text" class="form-control" name="extendedKey" placeholder="键" value="${key}" required>
             </div>
             <div class="col-md-6">
                  <input type="text" class="form-control" name="extendedValue" placeholder="值" value="${value}">
             </div>
             <div class="col-md-3 text-end">
                 <button type="button" class="btn btn-sm btn-danger remove-extended">
                     <i class="bi bi-trash"></i> 删除
                 </button>
             </div>
         </div>
       `;
    $('#extendedPropertiesContainer').append(property);
}

function detailFormatter(index, row) {
    if (!row.extended || Object.keys(row.extended).length === 0) {
        return '<div class="text-muted p-3 text-center">暂无扩展数据</div>';
    }

    const map = new Map(Object.entries(row.extended));
    const html = [];

    html.push('<div class="p-3 bg-light rounded border">');
    html.push('<div class="row g-3">');

    map.forEach((value, key) => {
        html.push(`<div class="col-12 col-md-4">`);
        html.push(`<div class="bg-white p-2 rounded border h-100 shadow-sm">`);
        html.push(`<div class="text-muted big fw-bold mb-1">${key}</div>`);
        html.push(`<div class="text-dark text-break" style="word-break: break-all; white-space: pre-wrap;">${value}</div>`);
        html.push(`</div>`);
        html.push(`</div>`);
    });
    html.push('</div>');
    html.push('</div>');
    return html.join('');
}

function initGroupKeySelect() {
    let $select = $(".alk-select-groupKey");
    $select.each(function () {
        let tomSelect = new TomSelect(this, {
            plugins: ['remove_button'],
            placeholder: '选择规则组',
            options: [{value: 'default_', text: `<div><span class="badge bg-secondary">默认组</span></div>`}],
            items: ['default_'],
            render: {
                option: function (data, escape) {
                    return `${data.text}`;
                },
                item: function (data, escape) {
                    return `${data.text}`;
                }
            }
        });
    });
    httpClient('/rule/group/map', 'GET', null, function (data) {
        sessionStorage.setItem('alatka.rule.groupKeyMapping', JSON.stringify(data));

        const options = Object.entries(data).map(([key, value]) => ({
            value: key,
            text: `<div><span class="badge bg-primary">${value}</span></div>`
        }));
        $select.each(function () {
            this.tomselect.addOption(options);
            this.tomselect.setValue([Object.values(this.tomselect.options)[0].value]);
            $(this).data('alk-init-value', Object.values(this.tomselect.options)[0].value);
        });
    });
}

function groupKeyFormatter(arg) {
    let mapping = JSON.parse(sessionStorage.getItem('alatka.rule.groupKeyMapping') || {});
    return `<span class="badge bg-primary">${mapping[arg] || arg}</span>`;
}

function enabledFormatter(arg) {
    if (arg === true || arg === 'true') {
        return `<span class="badge bg-success">正常</span>`;
    }
    return `<span class="badge bg-danger">禁用</span>`;
}

function initEnabledSelect() {
    $(".alk-select-enabled").each(function () {
        let tomSelect = new TomSelect(this, {
            plugins: ['remove_button'],
            placeholder: '选择状态',
            options: [{value: 'true', text: '正常'}, {value: 'false', text: '禁用'}],
            items: ['true'],
            render: {
                option: function (data, escape) {
                    return `<div>${enabledFormatter(data.value)}</div>`;
                },
                item: function (data, escape) {
                    return `<div>${enabledFormatter(data.value)}</div>`;
                }
            }
        });
        $(this).data('alk-init-value', tomSelect.items[0]);
    });
}
