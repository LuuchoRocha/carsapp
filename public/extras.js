function reactivarFields() {
    document.getElementById("doors").disabled = true;
};

function escribir(dato, contenedor) {
    $("#" + contenedor).html(dato);
};

function traer(url, contenedor, formulario) {
    if (formulario) {
        $.ajax({
            url: url, 
            method: 'GET',
            async: true,
            data: $("#" + formulario).serialize(), 
            success: function(returnData) {escribir(returnData, contenedor);}
        });
    } else {
        $.ajax({
            url: url,
            method: 'GET',
            async: true,
            success: function(returnData) {escribir(returnData, contenedor);}
        });
    };    
};

function postear(url, contenedor, formulario) {
    $.ajax({
        url: url, 
        method: 'POST',
        async: true,
        data: $("#" + formulario).serialize(), 
        success: function(returnData) {escribir(returnData, contenedor);}
    });
};

function eliminar(url, id) {
    $.ajax({
        method: 'DELETE',
        url: url + "/" + document.getElementById(id).value,
        async: true,
        success: function(returnData) {
                        alert(returnData);
                        document.location='/';
                 }
    });
};

function eliminarDirecto(url, id) {
    $.ajax({
        method: 'DELETE',
        url: url + "/" + id,
        async: true,
        success: function(returnData) {
                        alert(returnData);
                        document.location='/';
                 }
    });
};
