//这里都是公共的，一些枚举

// recommend，推荐商家状态枚举
var RECOMMEND_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '展示'
    },
    '_20': {
        id: 20,
        name: '不展示'
    }
};

//bannerStatus 轮播图状态枚举
var BANNER_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '展示'
    },
    '_20': {
        id: 20,
        name: '不展示'
    }
};


//noticeStatus 公告状态枚举
var NOTICE_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '展示'
    },
    '_20': {
        id: 20,
        name: '不展示'
    }
};


//gasType 气质类型枚举
var GAS_TYPE_ENUM = {
    '_10': {
        id: 10,
        name: '煤层气'
    },
    '_20': {
        id: 20,
        name: '焦炉气'
    },
    '_30': {
        id: 30,
        name: '页岩气'
    },
    '_40': {
        id: 40,
        name: '进口气'
    },
    '_50': {
        id: 50,
        name: '管道气'
    },
    '_60': {
        id: 60,
        name: '其他'
    }, '_70': {
        id: 70,
        name: '井口气'
    },
    '_80': {
        id: 80,
        name: '油田气'
    },
    '_90': {
        id: 90,
        name: '煤制气'
    }
};

//distanceChargeType 运费类型
var DISTANCE_CHARGE_TYPE_ENUM = {
    '_10': {
        id: 10,
        name: '一口价',
        unit: '元/吨'
    },
    '_true': {
        id: 10,
        name: '一口价',
        unit: '元/吨'
    },
    '_20': {
        id: 20,
        name: '单价',
        unit: '元/吨*公里'
    },
    '_false': {
        id: 20,
        name: '单价',
        unit: '元/吨*公里'
    }
};

//quoteStatus 报价单状态
var QUOTE_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '待复核'
    },
    '_20': {
        id: 20,
        name: '已通过'
    },
    '_30': {
        id: 30,
        name: '未通过'
    }
};

//goodsPriceSatus 商品报价状态
var GOODS_PRICE_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '待上架'
    },
    '_20': {
        id: 20,
        name: '上架审核中'
    },
    '_30': {
        id: 30,
        name: '已上架'
    },
    '_40': {
        id: 40,
        name: '已驳回'
    },
    '_50': {
        id: 50,
        name: '已下架'
    }
};

//orderStatus 订单状态枚举
var ORDER_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '待分配运输资源'
    },
    '_20': {
        id: 20,
        name: '待确认'
    },
    '_30': {
        id: 30,
        name: '出发确认'
    },
    '_40': {
        id: 40,
        name: '出厂确认'
    },
    '_50': {
        id: 50,
        name: '进站确认'
    },
    '_60': {
        id: 60,
        name: '收货确认'
    },
    '_70': {
        id: 70,
        name: '卖家确认'
    },
    '_80': {
        id: 80,
        name: '已完成'
    },
    '_90': {
        id: 90,
        name: '已关闭'
    }
};

//transportType 运输方式枚举
var TRANSPORT_TYPE_ENUM = {
    '_10': {
        id: 10,
        name: '不限'
    },
    '_20': {
        id: 20,
        name: '自提'
    },
    '_30': {
        id: 30,
        name: '配送'
    },
    '_40': {
        id: 40,
        name: '委托物流'
    }
};


//payType 支付方式枚举
var PAY_TYPE_ENUM = {
    '_10': {
        id: 10,
        name: '平台转帐'
    },
    '_20': {
        id: 20,
        name: '线下支付'
    }
};

//isDispatching 是否支持配送
var IS_DISPATCHING_ENUM = {
    '_10': {
        id: 10,
        name: '支持'
    },
    '_true': {
        id: 10,
        name: '支持'
    },
    '_20': {
        id: 20,
        name: '不支持'
    },
    '_false': {
        id: 20,
        name: '不支持'
    }
};

//inquiryStatus 询价单的状态
var INQUIRY_STATUS_ENUM = {
    '_10': {
        id: 10,
        name: '未回复'
    },
    '_20': {
        id: 20,
        name: '已回复'
    },
};

var QUALIFICATION_STATUS = {
    '_isBuyer': {
        id: 0,
        name: '买家资质,'
    },
    '_isSeller': {
        id: 1,
        name: '卖家资质,'
    },
     '_isTransport': {
        id: 1,
        name: '运输资质,'
    },
};
