package ar.edu.unq.epersgeist.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Paginador {

    public static Pageable paginar(Direccion direccion, int pagina, int cantidadPorPagina){

        var pag = pagina < 1 ? 1 : pagina;
        var cant = cantidadPorPagina <= 0 ? 5 : cantidadPorPagina;

        Sort sort = (direccion == Direccion.DESCENDENTE)
                ? Sort.by("id").descending()
                : Sort.by("id").ascending();

        Pageable pageable = PageRequest.of(pag - 1, cant, sort);

        return pageable;
    }

}