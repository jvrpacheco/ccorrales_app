package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.NewPriceResponse;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.model.UnitsResponse;
import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;
import com.corporacioncorrales.cotizacionesapp.model.WarehouseResponse;
import com.corporacioncorrales.cotizacionesapp.networking.NewPriceApi;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.networking.UnitsApi;
import com.corporacioncorrales.cotizacionesapp.networking.VirtualStockApi;
import com.corporacioncorrales.cotizacionesapp.networking.WarehousesApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by victor on 8/20/16.
 */
public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.QuotationViewHolder> {

    Context mContext;
    ArrayList<ProductsResponse> productsList;
    private ProgressBar progressBar;
    private TextView tvTotalProductos;
    private TextView tvMontoTotal;
    private TextView tvIndicadorSaldoDisponible;
    private String quantityInserted = "";
    private String precioIngresado = "";
    private Boolean esPrecioMenorAlLimite;
    private String tipoDocumento;
    private UnitsAdapter unitsAdapter;
    private ProgressBar productsProgressBar;
    private WarehouseAdapter warehousesAdapter;
    private boolean todosIsChecked = false;


    public QuotationAdapter(Context mContext, ArrayList<ProductsResponse> productsList, TextView tvTotalProductos,
                            TextView tvMontoTotal, TextView tvIndicadorSaldoDisponible, ProgressBar productsProgressBar  ) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.tvTotalProductos = tvTotalProductos;
        this.tvMontoTotal = tvMontoTotal;
        this.tvIndicadorSaldoDisponible = tvIndicadorSaldoDisponible;
        this.productsProgressBar = productsProgressBar;

    }

    @Override
    public QuotationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_row_item, parent, false);
        QuotationViewHolder productsViewHolder = new QuotationViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final QuotationViewHolder holder, final int position) {

        tipoDocumento = Singleton.getInstance().getTipoDocumento();
        final ProductsResponse product = productsList.get(position);


        holder.tvId.setText(product.getId());
        holder.tvUnidadVenta.setText(product.getNuevaPresentacion());  //al inicio es getPresentacion()
        holder.tvDescription.setText(product.getNombre());

        //Nuevo Precio y Cantidad Solicitada
        if (product.getCantidadSolicitada() == null && product.getNuevoPrecio() != null) {
            holder.ivChangeQuantity.setVisibility(View.GONE);
            holder.tvCantidadSolicitada.setText("0");
            holder.tvNewPrice.setText(product.getPrecioRecalculado());

        } else if (product.getCantidadSolicitada() != null && product.getNuevoPrecio() == null) {
            if (product.getCantidad().equals("0")) {
                holder.ivChangeQuantity.setVisibility(View.GONE);
                holder.tvCantidadSolicitada.setText("0");
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            } else {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            }

        } else if (product.getCantidadSolicitada() == null && product.getNuevoPrecio() == null) {
            if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_boleta) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                if (product.getCantidad().equals("0")) {
                    holder.ivChangeQuantity.setVisibility(View.GONE);
                    holder.tvCantidadSolicitada.setText("0");
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                } else {
                    holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                    holder.tvCantidadSolicitada.setText("0");
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                }
            } else if (tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText("0");
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            }

        } else if (product.getCantidadSolicitada() != null && product.getNuevoPrecio() != null) {
            if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_boleta) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                if (product.getNuevaCantidad().equals("0")) {
                    holder.ivChangeQuantity.setVisibility(View.GONE);
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                } else {
                    holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                }
            } else if (tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            }
        }

        if (product.getEsPrecioMenorAlLimite()) {
            holder.ivChangePrice.setImageResource(R.drawable.hand_red_52);
        } else {
            holder.ivChangePrice.setImageResource(R.drawable.hand_green_52);
        }

        //Stock
        //holder.tvQuantity.setText(product.getCantidad());
        holder.tvQuantity.setText(product.getNuevaCantidad());   //product.getNuevaCantidad() si es null obtendra el valor original de getCantidad()

        //Precio total
        //if(Integer.parseInt(product.getCantidad())>0) {
        try {
            Double total = 0.00;
            if (product.getCantidadSolicitada() == null && product.getNuevoPrecio() != null) {
                holder.tvCantidadSolicitada.setText("0");
                //holder.tvNewPrice.setText(product.getNuevoPrecio());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
                holder.tvTotalPrice.setText("0.00");

            } else if (product.getCantidadSolicitada() != null && product.getNuevoPrecio() == null) {
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
                total = Double.valueOf(product.getPrecioRecalculado()) * Integer.valueOf(product.getCantidadSolicitada());
                holder.tvTotalPrice.setText(String.format(Constants.round_two_decimals, total));

            } else if (product.getCantidadSolicitada() == null && product.getNuevoPrecio() == null) {
                holder.tvCantidadSolicitada.setText("0");
                product.setCantidadSolicitada("0");
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
                total = Double.valueOf(product.getPrecioRecalculado());
                holder.tvTotalPrice.setText(String.format(Constants.round_two_decimals, total));

            } else if (product.getCantidadSolicitada() != null && product.getNuevoPrecio() != null) {
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
                total = Double.valueOf(product.getPrecioRecalculado()) * Integer.valueOf(product.getCantidadSolicitada());
                holder.tvTotalPrice.setText(String.format(Constants.round_two_decimals, total));
            }

        } catch (Exception ex) {
            Log.e(Constants.log_arrow_error, ex.toString());
        }

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setSelected(false);
                removeItem(product);
                ProductsFragment.productsAdapter.refreshItem(product, false);  // update the left list!
            }
        });

        holder.ivWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectWarehouseDialog(mContext, product.getId(), product.getNuevaUnidad(), product);
            }
        });

        holder.ivChangePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!product.getPrecioProductResponse().isEmpty() && !product.getPre_inferior().isEmpty()) {
                    showChangePriceDialog(mContext, product, position, product.getPrecioProductResponse(), product.getPre_inferior());
                } else {
                    Common.showToastMessage(mContext, "No existe precio o precio inferior para este producto");
                }
            }
        });

        holder.ivChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_boleta) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                    if (!product.getNuevaCantidad().isEmpty() && Integer.parseInt(product.getNuevaCantidad()) > 0) {
                        showChangeQuantityDialog(mContext, product, position);
                    }
                } else if (tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                    showChangeQuantityDialog(mContext, product, position);
                }
            }
        });

        holder.ivVirtualStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVirtualStockDialog(mContext,
                        Singleton.getInstance().getUserCode(),
                        Singleton.getInstance().getRubroSelected(),
                        product.getId());
            }
        });

        holder.ivShowDialogUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnidadDeMedidaDialog(mContext, product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public ArrayList<ProductsResponse> getQuotationProductsList() {
        return productsList;
    }

    public static class QuotationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvId, tvUnidadVenta, tvDescription, tvPrice, tvQuantity, tvNewPrice, tvCantidadSolicitada, tvTotalPrice;
        ImageView ivRemove , ivWarehouse, ivArrival, ivChangePrice, ivChangeQuantity, ivVirtualStock, ivShowDialogUnidad;


        public QuotationViewHolder(View view) {
            super(view);
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvUnidadVenta = (TextView) view.findViewById(R.id.tvUnidadVenta);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvNewPrice = (TextView) view.findViewById(R.id.tvNewPrice);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);
            tvCantidadSolicitada = (TextView) view.findViewById(R.id.tvCantidadSolicitada);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            ivWarehouse = (ImageView) view.findViewById(R.id.ivWarehouse);
            ivChangePrice = (ImageView) view.findViewById(R.id.ivChangePrice);
            ivChangeQuantity = (ImageView) view.findViewById(R.id.ivChangeQuantity);
            ivVirtualStock = (ImageView) view.findViewById(R.id.ivVirtualStock);
            ivShowDialogUnidad = (ImageView) view.findViewById(R.id.ivShowDialogUnidad);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeItem(ProductsResponse product) {
        int currPosition = productsList.indexOf(product);
        if (currPosition > -1) {
            productsList.remove(currPosition);
            notifyItemRemoved(currPosition);
            updateTotalProducts();
            resetProduct(product);
        }
    }

    public void addItem(int position, ProductsResponse product) {
        productsList.add(position, product);
        notifyItemInserted(position);
        updateTotalProducts();
    }

    public void refreshItems() {
        //notifyItemChanged(position);
        notifyDataSetChanged();
        updateTotalProducts();
    }

    public void resetProduct(ProductsResponse product) {
        product.setNuevoPrecio(product.getPrecioProductResponse());
        product.setEsPrecioMenorAlLimite(false);
        product.setCantidadSolicitada("0");

        product.setNuevaUnidad(product.getIdUnidad());
        product.setNuevaPresentacion(product.getPresentacionUnidad());
        product.setNuevaCantidad(product.getCantidad());
        product.setPrecioRecalculado(product.getPrecioProductResponse());
        product.setIdAlmacenAsociado(Constants.todosLosAlmacenes);
    }

    public void resetProducts() {
        for (int i = 0; i < productsList.size(); i++) {
            resetProduct(productsList.get(i));
        }
    }

    private void showChangeQuantityDialog(final Context context, final ProductsResponse product, final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quotation_change_quantity);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);

        final TextView tvProductDes = (TextView) dialog.findViewById(R.id.tvProductDes);
        final EditText edtQuantity = (EditText) dialog.findViewById(R.id.edtQuantity);
        final TextView tvStock = (TextView) dialog.findViewById(R.id.tvStock);
        final TextView tvCompareResult = (TextView) dialog.findViewById(R.id.tvCompareResult);
        final TextView tvProductCode = (TextView) dialog.findViewById(R.id.tvProductCode);
        final ImageView ivUpQuantity = (ImageView) dialog.findViewById(R.id.ivUpQuantity);
        final ImageView ivDownQuantity = (ImageView) dialog.findViewById(R.id.ivDownQuantity);
        final String stock = product.getNuevaCantidad();


        tvProductDes.setText(product.getNombre());
        tvProductCode.setText(product.getId());
        quantityInserted = product.getCantidadSolicitada();
        tvStock.setText(stock);

        if (Integer.valueOf(quantityInserted) > 0) {
            edtQuantity.setText(quantityInserted);
        } else {
            edtQuantity.setText(quantityInserted);
            tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_mayor_a_cero));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            btnAcceptDialog.setEnabled(false);
        }

        checkQuantity(context, tvCompareResult, btnAcceptDialog, quantityInserted, stock);

        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String qInserted = s.toString();
                checkQuantity(context, tvCompareResult, btnAcceptDialog, qInserted, stock);
            }
        });

        ivUpQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int newValue = Integer.valueOf(quantityInserted) + Constants.baseInteger;
                    quantityInserted = String.valueOf(newValue);
                    edtQuantity.setText(String.valueOf(newValue));
                } catch (Exception e) {
                    Log.e(Constants.log_arrow_error, e.toString());
                }
            }
        });



        ivDownQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int newValue = Integer.valueOf(quantityInserted) - Constants.baseInteger;
                    if (newValue >= 0) {
                        quantityInserted = String.valueOf(newValue);
                        edtQuantity.setText(String.valueOf(newValue));
                    }
                } catch (Exception e) {
                    Log.e(Constants.log_arrow_error, e.toString());
                }
            }
        });


        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                product.setCantidadSolicitada(quantityInserted);
                //refreshItem(position, product);
                refreshItems();
                dialog.dismiss();
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Common.hideKeyboardOnDialog(dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void checkQuantity(Context context, TextView tvCompareResult, Button btnAcceptDialog, String qInserted, String stock) {
        if (!qInserted.isEmpty()) {
            if (Integer.parseInt(qInserted) > 0) {
                if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_boleta) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                    if (Integer.parseInt(qInserted) <= Integer.parseInt(stock)) {
                        tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_permitida));
                        tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                        btnAcceptDialog.setEnabled(true);
                        quantityInserted = qInserted;
                    } else {
                        tvCompareResult.setText(String.format("%s %s %s", "El máximo permitido es de", stock, "productos."));
                        tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                        btnAcceptDialog.setEnabled(false);
                    }
                } else if (tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                    Log.d(Constants.log_arrow, "Preventa, no importa si no hay stock. Cualquier cantidad permitida");
                    tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_permitida));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                    btnAcceptDialog.setEnabled(true);
                    quantityInserted = qInserted;
                }
            } else if (qInserted.equals("0")) {
                tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_mayor_a_cero));
                tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                btnAcceptDialog.setEnabled(false);
            }
        } else {
            tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_vacia));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            btnAcceptDialog.setEnabled(false);
        }
    }

    private void showChangePriceDialog(final Context context, final ProductsResponse product, final int position, final String price, final String priceMinLimit) {
        final String tipoDocumento = Singleton.getInstance().getTipoDocumento();
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quotation_change_price1);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);
        final TextView tvPrecio = (TextView) dialog.findViewById(R.id.tvPrecio);
        final TextView tvPrecioLimiteInferior = (TextView) dialog.findViewById(R.id.tvPrecioLimInferior);
        final TextView tvPrecioIngresado = (TextView) dialog.findViewById(R.id.tvPrecioIngresado);
        final TextView tvCompareResult = (TextView) dialog.findViewById(R.id.tvCompareResult);
        final TextView tvProductDes = (TextView) dialog.findViewById(R.id.tvProductDes);
        final TextView tvProductCode = (TextView) dialog.findViewById(R.id.tvProductCode);
        final EditText edtPrice = (EditText) dialog.findViewById(R.id.edtPrice);
        final ImageView ivUpPrice = (ImageView) dialog.findViewById(R.id.ivUpPrice);
        final ImageView ivDownPrice = (ImageView) dialog.findViewById(R.id.ivDownPrice);
        final ImageView ivShowPricesHistory = (ImageView) dialog.findViewById(R.id.ivShowPricesHistory);
        final ImageView ivHidePricesHistory = (ImageView) dialog.findViewById(R.id.ivHidePricesHistory);
        final LinearLayout ll_pricesHistory = (LinearLayout) dialog.findViewById(R.id.ll_pricesHistory);
        final LinearLayout ll_prices = (LinearLayout) dialog.findViewById(R.id.ll_prices);
        final RecyclerView rvPricesHistory = (RecyclerView) dialog.findViewById(R.id.rvPricesHistory);

        try {
            tvPrecio.setText(String.format(Constants.round_three_decimals, Double.parseDouble(price)));
            tvPrecioLimiteInferior.setText(String.format(Constants.round_three_decimals, Double.parseDouble(priceMinLimit)));
            tvPrecioIngresado.setText(String.format(Constants.round_three_decimals, Double.parseDouble(product.getPrecioRecalculado())));

                    //el precio ingresado es... respecto al precio minimo
            String priceInserted0 = product.getPrecioRecalculado();
            String resultComparePrices = Common.comparePrices(Double.valueOf(priceInserted0), Double.valueOf(priceMinLimit));
            if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_boleta)) {
                if (resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                    precioIngresado = priceInserted0;
                    esPrecioMenorAlLimite = false;
                    btnAcceptDialog.setEnabled(true);

                } else if (resultComparePrices.equals(Constants.comparar_esMenor)) {
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    btnAcceptDialog.setEnabled(false);
                }

            } else if (tipoDocumento.equals(Constants.tipoDoc_proforma) || tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                if (resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                    precioIngresado = priceInserted0;
                    esPrecioMenorAlLimite = false;
                    btnAcceptDialog.setEnabled(true);
                } else if (resultComparePrices.equals(Constants.comparar_esMenor)) {
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    precioIngresado = priceInserted0;
                    esPrecioMenorAlLimite = true;
                    btnAcceptDialog.setEnabled(true);
                }
            }

        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }

        tvProductCode.setText(product.getId());
        tvProductDes.setText(product.getNombre());

        precioIngresado = product.getPrecioRecalculado();

        edtPrice.setText(String.valueOf(Double.parseDouble(precioIngresado)));
        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v("beforeTextChanged ---->", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("onTextChanged ---->", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceInserted = s.toString();

                if (!priceInserted.isEmpty() && !priceInserted.equals(".") && !priceInserted.equals(",")) {
                    tvPrecioIngresado.setText(priceInserted);
                    //el precio ingresado es... respecto al precio minimo
                    String resultComparePrices = Common.comparePrices(Double.valueOf(priceInserted), Double.valueOf(priceMinLimit));

                    if (tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_boleta)) {
                        if (resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                            precioIngresado = priceInserted;
                            esPrecioMenorAlLimite = false;
                            btnAcceptDialog.setEnabled(true);

                        } else if (resultComparePrices.equals(Constants.comparar_esMenor)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                            btnAcceptDialog.setEnabled(false);
                        }
                    } else if (tipoDocumento.equals(Constants.tipoDoc_proforma) || tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                        if (resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                            precioIngresado = priceInserted;
                            esPrecioMenorAlLimite = false;
                            btnAcceptDialog.setEnabled(true);
                        } else if (resultComparePrices.equals(Constants.comparar_esMenor)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                            precioIngresado = priceInserted;
                            esPrecioMenorAlLimite = true;
                            btnAcceptDialog.setEnabled(true);
                        }
                    }
                } else {
                    tvPrecioIngresado.setText("---");
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_ingresado_vacio));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    btnAcceptDialog.setEnabled(false);
                }
            }
        });

        ivUpPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!precioIngresado.isEmpty() && !precioIngresado.equals(".")) {
                    try {
                        Double newValue = Double.valueOf(precioIngresado) + Constants.baseDouble;
                        String np = String.format(Constants.round_three_decimals, newValue);
                        edtPrice.setText(np.toString());
                        precioIngresado = np;
                    } catch (Exception e) {
                        Log.e(Constants.log_arrow_error, e.toString());
                    }
                }
            }
        });

        ivDownPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!precioIngresado.isEmpty() && !precioIngresado.equals(".")) {
                    try {
                        Double newValue = Double.valueOf(precioIngresado) - Constants.baseDouble;
                        if (newValue >= 0) {
                            String np = String.format(Constants.round_three_decimals, newValue);
                            edtPrice.setText(np.toString());
                            precioIngresado = np;
                        }
                    } catch (Exception e) {
                        Log.e(Constants.log_arrow_error, e.toString());
                    }
                }
            }
        });

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Common.isOnline(context)) {
                    getNewPriceRecalculatedFromChangePriceDialog(context, dialog, productsProgressBar, product, precioIngresado, btnAcceptDialog, btnCloseDialog);
                }
            }
        });

        ivShowPricesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_prices.setVisibility(View.GONE);
                ll_pricesHistory.setVisibility(View.VISIBLE);

                if (Common.isOnline(mContext)) {
                    loadPricesHistory(
                            rvPricesHistory,
                            Singleton.getInstance().getIdclientSelected(),
                            product.getId());
                }
            }
        });

        ivHidePricesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_prices.setVisibility(View.VISIBLE);
                ll_pricesHistory.setVisibility(View.GONE);
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Common.hideKeyboardOnDialog(dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getNewPriceRecalculatedFromChangePriceDialog(final Context context, final Dialog dialog, final ProgressBar newProgressBar, final ProductsResponse product, final String precioIngresado, Button btnAcceptDialog, Button btnCloseDialog) {
        newProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewPriceApi request = retrofit.create(NewPriceApi.class);
        Call<ArrayList<NewPriceResponse>> call = request.getNewPriceRecalculated(product.getId().trim(), product.getNuevaUnidad().trim(), precioIngresado);

        call.enqueue(new Callback<ArrayList<NewPriceResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NewPriceResponse>> call, Response<ArrayList<NewPriceResponse>> response) {
                if (response != null) {
                    if (response.body() != null) {
                        ArrayList<NewPriceResponse> newPriceReca = response.body();

                        if (newPriceReca.size() > 0) {
                            if (newPriceReca.get(0) != null && !newPriceReca.get(0).getImporteRecalculado().isEmpty()) {
                                String priceRecalculated = newPriceReca.get(0).getImporteRecalculado();

                                if (!priceRecalculated.isEmpty()) {
                                    product.setNuevoPrecio(precioIngresado);
                                    product.setPrecioRecalculado(String.format(Constants.round_three_decimals, Double.parseDouble(priceRecalculated)));
                                    product.setEsPrecioMenorAlLimite(esPrecioMenorAlLimite);
                                    refreshItems();
                                    dialog.dismiss();
                                }
                            }
                        } else {
                            Log.d(Constants.log_arrow_response, "Lo sentimos, el servidor no ha devuelto el precio.");
                            Common.showToastMessage(context, "Lo sentimos, el servidor no ha devuelto el precio.");
                        }
                        newProgressBar.setVisibility(View.GONE);
                    } else {
                        Log.d(Constants.log_arrow_response, "body null:" + response.raw().message());
                        Common.showToastMessage(context, "Error en el servidor");
                    }
                } else {
                    Common.showToastMessage(context, "Error en el servidor");
                    Log.e(Constants.log_arrow_response, response.message());
                    newProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NewPriceResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Common.showToastMessage(context, "Error en el servidor");
                Log.e(Constants.log_arrow_response, t.toString());
            }
        });
    }

    private void getNewPriceRecalculatedFromChangeUnitDialog(final UnitsResponse unitSelected, final Context context, final Dialog dialog, final ProgressBar newProgressBar, final ProductsResponse product, final String precioIngresado, Button btnAcceptDialog, Button btnCloseDialog) {
        newProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewPriceApi request = retrofit.create(NewPriceApi.class);
        Call<ArrayList<NewPriceResponse>> call = request.getNewPriceRecalculated(product.getId().trim(), unitSelected.getUnidad(), precioIngresado);

        call.enqueue(new Callback<ArrayList<NewPriceResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NewPriceResponse>> call, Response<ArrayList<NewPriceResponse>> response) {
                if (response != null) {
                    if (response.body() != null) {
                        ArrayList<NewPriceResponse> newPriceReca = response.body();

                        if (newPriceReca.size() > 0) {
                            if (newPriceReca.get(0) != null && !newPriceReca.get(0).getImporteRecalculado().isEmpty()) {
                                String priceRecalculated = newPriceReca.get(0).getImporteRecalculado();

                                if (!priceRecalculated.isEmpty()) {
                                    product.setNuevaUnidad(unitSelected.getUnidad());
                                    product.setNuevaPresentacion(unitSelected.getPresentacion());
                                    product.setNuevaCantidad(unitSelected.getStock());
                                    product.setPrecioRecalculado(String.format(Constants.round_three_decimals, Double.parseDouble(priceRecalculated)));
                                    product.setNuevoPrecio(product.getPrecioProductResponse());
                                    product.setCantidadSolicitada("0");
                                    refreshItems();
                                    dialog.dismiss();
                                }
                            }
                        } else {
                            Log.d(Constants.log_arrow_response, "Lo sentimos, el servidor no ha devuelto el precio.");
                            Common.showToastMessage(context, "Lo sentimos, el servidor no ha devuelto el precio.");
                        }
                        newProgressBar.setVisibility(View.GONE);
                    } else {
                        Log.d(Constants.log_arrow_response, "body null:" + response.raw().message());
                        Common.showToastMessage(context, "Error en el servidor");
                        newProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    Common.showToastMessage(context, "Error en el servidor");
                    Log.e(Constants.log_arrow_response, response.message());
                    newProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NewPriceResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Common.showToastMessage(context, "Error en el servidor");
                Log.e(Constants.log_arrow_response, t.toString());
            }
        });
    }

    private void loadPricesHistory(final RecyclerView rv, String idCliente, String idProduct) {

        rv.setHasFixedSize(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi request = retrofit.create(ProductsApi.class);
        Call<ArrayList<PricesHistoryResponse>> call = request.getPricesHistory(idCliente, idProduct);
        call.enqueue(new Callback<ArrayList<PricesHistoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<PricesHistoryResponse>> call, Response<ArrayList<PricesHistoryResponse>> response) {

                if (response != null) {
                    ArrayList<PricesHistoryResponse> pricesHistory = response.body();
                    pricesHistory = response.body();

                    if (pricesHistory != null && pricesHistory.size() > 0) {
                        PricesHistoryAdapter pricesHistoryAdapter = new PricesHistoryAdapter(mContext, pricesHistory);
                        rv.setAdapter(pricesHistoryAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rv.setLayoutManager(llm);
                    } else {
                        Log.d(Constants.log_arrow_response, "No se encontraron precios para este producto");
                        Common.showToastMessage(mContext, "No se encontraron precios para este producto");
                    }

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(mContext, "Error en el servidor");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PricesHistoryResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_response, "response null");
                Common.showToastMessage(mContext, "Error en el servidor");
            }
        });

    }

    private void updateTotalProducts() {
        if (tvMontoTotal != null) {
            if (productsList.size() > 0) {
                int cont = 0;
                int minQuantity = -1;
                Double suma = 0.00;
                Double precioTotalPorProducto = 0.00;
                String tipoDoc = Singleton.getInstance().getTipoDocumento();

                for (int i = 0; i < productsList.size(); i++) {
                    ProductsResponse product = productsList.get(i);

                    if (product.getCantidadSolicitada() != null && product.getPrecioRecalculado() != null) {
                        try {
                            Integer cantidadSolicitada = Integer.parseInt(product.getCantidadSolicitada());
                            precioTotalPorProducto = Double.parseDouble(product.getPrecioRecalculado()) * cantidadSolicitada;
                        } catch (Exception ex) {
                            Log.e(Constants.log_arrow_failure, ex.toString());
                        }
                    }

                    cont++;
                    suma = suma + precioTotalPorProducto;
                }

                tvTotalProductos.setText(String.valueOf(cont));
                tvMontoTotal.setText(String.format(Constants.round_two_decimals, suma));

                if (!Singleton.getInstance().getSaldoDisponibleCliente().isEmpty()) {
                    if (isUpToCreditLine(suma, Singleton.getInstance().getSaldoDisponibleCliente())) {
                        setSaldoDisponibleIndicator(false);
                    } else {
                        setSaldoDisponibleIndicator(true);
                    }
                }

            } else {
                tvTotalProductos.setText("0");
                tvMontoTotal.setText("0.00");
                setSaldoDisponibleIndicator(true);
            }
        }
    }


    private void showVirtualStockDialog(final Context context, String idUsuario, String idRubro, String idArticulo) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stock_virtual);

        final RecyclerView rvStockVirtual = (RecyclerView) dialog.findViewById(R.id.rvStockVirtual);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final TextView tvMsgNoVirtualStock = (TextView) dialog.findViewById(R.id.tvMsgNoVirtualStock);
        progressBar = (ProgressBar) dialog.findViewById(R.id.newProgressBar);

        if (Common.isOnline(mContext)) {
            getVirtualStock(context, idUsuario, idRubro, idArticulo, rvStockVirtual, tvMsgNoVirtualStock);
        }

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    ArrayList<WarehouseResponse> mWarehouses;
    private void getWarehousesStock(String idArticulo, String unidad, final RecyclerView rvWarehouses, final ProductsResponse product, final ImageView ivTodos) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WarehousesApi request = retrofit.create(WarehousesApi.class);
        Call<ArrayList<WarehouseResponse>> call = request.getWarehousesStock(idArticulo, unidad);

        call.enqueue(new Callback<ArrayList<WarehouseResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<WarehouseResponse>> call, Response<ArrayList<WarehouseResponse>> response) {
                if(response!=null) {
                    ArrayList<WarehouseResponse> warehouses = response.body();

                    if(warehouses!=null && warehouses.size()>0) {
                        mWarehouses = warehouses;

                        if(product.getIdAlmacenAsociado().equals(Constants.todosLosAlmacenes)) {
                            ivTodos.setBackgroundResource(R.drawable.checked_checkbox_50);
                        } else {
                            ivTodos.setBackgroundResource(R.drawable.unchecked_checkbox_50);

                            for(WarehouseResponse warehouse : mWarehouses) {
                                if(warehouse.getCodigo().equals(product.getIdAlmacenAsociado())) {
                                    warehouse.setSelected(true);
                                }
                            }
                        }
                        ivTodos.setEnabled(true);

                        warehousesAdapter = new WarehouseAdapter(mContext, mWarehouses, product, ivTodos);
                        rvWarehouses.setAdapter(warehousesAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rvWarehouses.setLayoutManager(llm);

                    } else {
                        Common.showToastMessageShort(mContext, "No existe informacion de almacenes para este producto");
                    }
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.GONE);
                    Common.logFailureServerCall(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WarehouseResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Common.logFailureServerCall(mContext, t.getMessage());
            }
        });
    }

    private void getVirtualStock(final Context context, String idUsuario, String idRubro, String idArticulo, final RecyclerView rvStockVirtual, final TextView tvMsgNoVirtualStock) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VirtualStockApi request = retrofit.create(VirtualStockApi.class);
        Call<ArrayList<VirtualStockResponse>> call = request.getVirtualStock(idUsuario, idRubro, idArticulo);

        call.enqueue(new Callback<ArrayList<VirtualStockResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<VirtualStockResponse>> call, Response<ArrayList<VirtualStockResponse>> response) {
                if (response != null) {
                    ArrayList<VirtualStockResponse> virtualStockList = response.body();
                    if (virtualStockList != null && virtualStockList.size() > 0) {
                        tvMsgNoVirtualStock.setVisibility(View.GONE);
                        VirtualStockAdapter virtualStockAdapter = new VirtualStockAdapter(mContext, virtualStockList);
                        rvStockVirtual.setAdapter(virtualStockAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rvStockVirtual.setLayoutManager(llm);
                    } else {
                        tvMsgNoVirtualStock.setVisibility(View.VISIBLE);
                        Log.d(Constants.log_arrow_response, context.getResources().getString(R.string.sin_stock_virtual));
                    }
                    progressBar.setVisibility(View.GONE);

                } else {
                    Common.showToastMessage(context, "Error en el servidor");
                    Log.e(Constants.log_arrow_response, response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VirtualStockResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Common.logFailureServerCall(mContext, t.getMessage());
            }
        });

    }

    //boolean isTodosChecked = false;
    public void showSelectWarehouseDialog(final Context context, String idArticulo, String unidad, final ProductsResponse product) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warehouse_selection);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnCancel);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final RecyclerView rvWarehouses = (RecyclerView) dialog.findViewById(R.id.rvWarehouses);
        final ImageView ivTodos = (ImageView) dialog.findViewById(R.id.ivTodos);
        progressBar = (ProgressBar) dialog.findViewById(R.id.newProgressBar);

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        if(product.getIdAlmacenAsociado().equals(Constants.todosLosAlmacenes))
            setTodosChecked(true, ivTodos);

        /*if(product.getIdAlmacenAsociado().equals(Constants.todosLosAlmacenes)) {
            setTodosChecked(true, ivTodos);
        } else {
            if(warehousesAdapter.getSelectedWarehouse()!=null) {
                warehousesAdapter.setWarehouseSelectedOnList(product.getIdAlmacenAsociado());
            } else {

            }
            setTodosChecked(false, ivTodos);
        }*/

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(warehousesAdapter!=null) {
                    WarehouseResponse warehouseSelected = warehousesAdapter.getSelectedWarehouse();

                    if(todosIsChecked) {
                        // si cuando marcas 'Todos' algun Almacen estaba seleccionado...
                        /*if(warehouseSelected!=null) {
                            warehouseSelected.setSelected(false);
                            //asignamos el id del almacen al producto seleccionado
                            product.setIdAlmacenAsociado(warehouseSelected.getCodigo());
                        }*/
                        product.setIdAlmacenAsociado(Constants.todosLosAlmacenes);
                        product.setCantidadSolicitada("0");
                        product.setNuevaCantidad(product.getCantidad());
                        //refrescamos la grilla de almacenes
                        //warehousesAdapter.notifyDataSetChanged();
                        refreshItems();
                    } else {
                        product.setCantidadSolicitada("0");
                        product.setNuevaCantidad(warehouseSelected.getStock());
                        product.setIdAlmacenAsociado(warehouseSelected.getCodigo());
                        //warehouseSelected.setSelected(true);
                        refreshItems();
                    }
                }

                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ivTodos.setEnabled(false);
        ivTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!todosIsChecked) {
                    ivTodos.setBackgroundResource(R.drawable.checked_checkbox_50);
                    todosIsChecked = true;
                    warehousesAdapter.unselectWarehouses();
                } else {
                    if(warehousesAdapter.getSelectedWarehouse()==null) {
                        Common.showToastMessage(mContext, "Por favor seleccione una opción.");
                    } else {
                        ivTodos.setBackgroundResource(R.drawable.unchecked_checkbox_50);
                        todosIsChecked = false;
                    }
                }
            }
        });

        if(Common.isOnline(context)) {
            getWarehousesStock(idArticulo, unidad, rvWarehouses, product, ivTodos);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setTodosChecked(boolean isChecked, ImageView ivTodos) {
        if(isChecked) {
            ivTodos.setBackgroundResource(R.drawable.checked_checkbox_50);
        } else {
            ivTodos.setBackgroundResource(R.drawable.unchecked_checkbox_50);
        }
    }

    public void showUnidadDeMedidaDialog(final Context context, final ProductsResponse product) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_unidad_venta);

        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnClose);
        final ProgressBar newProgressBar = (ProgressBar) dialog.findViewById(R.id.newProgressBar);
        final ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final RecyclerView rvUnitsPerArticle = (RecyclerView) dialog.findViewById(R.id.rvUnitsPerArticle);
        final TextView tvZoomDialogTitle = (TextView) dialog.findViewById(R.id.tvZoomDialogTitle);
        final TextView tvSelectedUnit = (TextView) dialog.findViewById(R.id.tvSelectedUnit);

        tvZoomDialogTitle.setText(String.format("%s - %s", context.getString(R.string.unidad_venta_tittle), product.getId()));

        //seteando unidad por defecto
        tvSelectedUnit.setText(String.format("Codigo: %s, Presentacion: %s, Stock: %s", product.getIdUnidad(), product.getPresentacionUnidad(), product.getCantidad()));

        if (Common.isOnline(context)) {
            getUnitsPerArticle(context, newProgressBar, product.getId(), rvUnitsPerArticle, tvSelectedUnit);
        }

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (unitsAdapter != null && unitsAdapter.getUnitSelected() != null) {

                    if (Common.isOnline(context)) {
                        UnitsResponse unitSelected = unitsAdapter.getUnitSelected();

                        if (!product.getNuevoPrecio().isEmpty()) {
                            getNewPriceRecalculatedFromChangeUnitDialog(unitSelected, context, dialog, productsProgressBar, product, product.getNuevoPrecio(), btnAcceptDialog, btnCloseDialog);
                        } else {
                            getNewPriceRecalculatedFromChangeUnitDialog(unitSelected, context, dialog, productsProgressBar, product, product.getPrecioProductResponse(), btnAcceptDialog, btnCloseDialog);
                        }

                    }

                    /*UnitsResponse unitSelected = unitsAdapter.getUnitSelected();
                    product.setNuevaUnidad(unitSelected.getUnidad());
                    product.setNuevaPresentacion(unitSelected.getPresentacion());
                    product.setNuevaCantidad(unitSelected.getStock());
                    product.setPrecioRecalculado(null);
                    product.setNuevoPrecio(product.getPrecio());
                    product.setCantidadSolicitada("0");
                    refreshItems();
                    dialog.dismiss();*/
                }
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getUnitsPerArticle(final Context context, final ProgressBar newProgressBar, String idArticulo, final RecyclerView rvUnitsPerArticle, final TextView tvSelectedUnit) {
        newProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UnitsApi request = retrofit.create(UnitsApi.class);
        Call<ArrayList<UnitsResponse>> call = request.getUnits(idArticulo);

        call.enqueue(new Callback<ArrayList<UnitsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<UnitsResponse>> call, Response<ArrayList<UnitsResponse>> response) {

                if (response != null) {
                    ArrayList<UnitsResponse> unitsPerArticleList = response.body();

                    if (unitsPerArticleList != null && unitsPerArticleList.size() > 0) {
                        unitsAdapter = new UnitsAdapter(context, unitsPerArticleList, tvSelectedUnit);
                        rvUnitsPerArticle.setAdapter(unitsAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rvUnitsPerArticle.setLayoutManager(llm);
                    } else {
                        /*Log.d(Constants.log_arrow_response, "No se encontro stock virtual para este producto");
                        Common.showToastMessageShort(context, "No se encontro stock virtual para este producto");*/
                        Log.d(Constants.log_arrow_response, "Error en el servidor");
                        Common.showToastMessageShort(context, "Error en el servidor");
                    }
                    newProgressBar.setVisibility(View.GONE);

                } else {
                    Common.logFailureServerCall(mContext, response.message());
                    newProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UnitsResponse>> call, Throwable t) {
                newProgressBar.setVisibility(View.GONE);
                Common.logFailureServerCall(mContext, t.getMessage());
            }
        });
    }

    private Boolean isUpToCreditLine(Double montoTotal, String creditLine) {
        Boolean upToCreditLine = false;
        try {
            Double difference = Double.valueOf(creditLine) - montoTotal;
            if (difference < 0) {
                upToCreditLine = true;
            }
        } catch (Exception ex) {
            Log.e(Constants.log_arrow, ex.toString());
        }
        return upToCreditLine;
    }

    private void setSaldoDisponibleIndicator(Boolean saldoSuficiente) {
        if (saldoSuficiente) {
            tvIndicadorSaldoDisponible.setText(mContext.getString(R.string.saldo_disponible_suficiente));
            tvIndicadorSaldoDisponible.setTextColor(ContextCompat.getColor(mContext, R.color.verde));
        } else {
            tvIndicadorSaldoDisponible.setText(mContext.getString(R.string.saldo_disponible_insuficiente));
            tvIndicadorSaldoDisponible.setTextColor(ContextCompat.getColor(mContext, R.color.rojo));
        }
    }
}
