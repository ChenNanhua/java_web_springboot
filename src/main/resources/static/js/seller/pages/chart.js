$(document).ready(function () {

    "use strict";

    $.get("/sellerRecordData", function (totalData) {
        console.log(totalData)
        let x = totalData.split("#")[0].split(",");
        let y = totalData.split("#")[1].split(",");
        console.log(x)
        let borderColor = []
        let backgroundColor = []
        for (let i = 0; i < x.length; i++) {
            let arr = MyRgba()
            borderColor.concat(arr[0])
            backgroundColor = backgroundColor.concat(arr[1])
        }

        new Chart(document.getElementById("chart2"), {
            "type": "bar",
            "data": {
                "labels": x,
                "datasets": [{
                    "label": "卖出数量",
                    "data": y,
                    "fill": false,
                    "backgroundColor": backgroundColor,
                    "borderColor": borderColor,
                    "borderWidth": 1
                }]
            },
            "options": {
                "scales": {
                    "yAxes": [{
                        "ticks": {
                            "beginAtZero": true,
                            "userCallback": function (label, index, labels) {
                                // when the floored value is the same as the value we have a whole number
                                if (Math.floor(label) === label) {
                                    return label;
                                }

                            },
                        }
                    }]
                }
            }
        });
    })
});


//rgb颜色随机
function MyRgba() {
    let r = Math.floor(Math.random() * 256);
    let g = Math.floor(Math.random() * 256);
    let b = Math.floor(Math.random() * 256);
    let a = 0.2
    return ['rgba(' + r + ',' + g + ',' + b + ',' + ')',
        'rgba(' + r + ',' + g + ',' + b + ',' + a + ')'];
}