package com.sistema.apirestaurante.dtos;

public class VentasDiarias {
    private int cantidadPedidosHoy;
    private double ingresosObtenidos;
    private int productosVendidos;
    private int pedidosEnProceso;
    private int pedidosPendientesDePago;
    private int pedidosTerminados;

    public VentasDiarias() {

    }

    public VentasDiarias(int cantidadPedidosHoy, double ingresosObtenidos, int productosVendidos, int pedidosEnProceso, int pedidosPendientesDePago, int pedidosTerminados) {
        this.cantidadPedidosHoy = cantidadPedidosHoy;
        this.ingresosObtenidos = ingresosObtenidos;
        this.productosVendidos = productosVendidos;
        this.pedidosEnProceso = pedidosEnProceso;
        this.pedidosPendientesDePago = pedidosPendientesDePago;
        this.pedidosTerminados = pedidosTerminados;
    }

    public int getCantidadPedidosHoy() {
        return cantidadPedidosHoy;
    }

    public void setCantidadPedidosHoy(int cantidadPedidosHoy) {
        this.cantidadPedidosHoy = cantidadPedidosHoy;
    }

    public double getIngresosObtenidos() {
        return ingresosObtenidos;
    }

    public void setIngresosObtenidos(double ingresosObtenidos) {
        this.ingresosObtenidos = ingresosObtenidos;
    }

    public int getProductosVendidos() {
        return productosVendidos;
    }

    public void setProductosVendidos(int productosVendidos) {
        this.productosVendidos = productosVendidos;
    }

    public int getPedidosEnProceso() {
        return pedidosEnProceso;
    }

    public void setPedidosEnProceso(int pedidosEnProceso) {
        this.pedidosEnProceso = pedidosEnProceso;
    }

    public int getPedidosPendientesDePago() {
        return pedidosPendientesDePago;
    }

    public void setPedidosPendientesDePago(int pedidosPendientesDePago) {
        this.pedidosPendientesDePago = pedidosPendientesDePago;
    }

    public int getPedidosTerminados() {
        return pedidosTerminados;
    }

    public void setPedidosTerminados(int pedidosTerminados) {
        this.pedidosTerminados = pedidosTerminados;
    }
}