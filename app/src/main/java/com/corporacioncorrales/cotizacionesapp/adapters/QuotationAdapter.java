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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.NewPriceResponse;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.model.UnitsResponse;
import com.corporacioncorrales.cotizacionesapp.model.VirtualStockResponse;
import com.corporacioncorrales.cotizacionesapp.networking.NewPriceApi;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.networking.UnitsApi;
import com.corporacioncorrales.cotizacionesapp.networking.VirtualStockApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
    private AppCompatActivity mActivity;
    private TextView tvTotalProductos;
    private TextView tvMontoTotal;
    private TextView tvIndicadorSaldoDisponible;
    private String quantityInserted = "";
    private String precioIngresado = "";
    private Boolean esPrecioMenorAlLimite;
    private String tipoDocumento;
    private UnitsAdapter unitsAdapter;
    private ProgressBar productsProgressBar;

    public QuotationAdapter(Context mContext, ArrayList<ProductsResponse> productsList, TextView tvTotalProductos, TextView tvMontoTotal, TextView tvIndicadorSaldoDisponible, ProgressBar productsProgressBar) {
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
        if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()!=null) {

            holder.ivChangeQuantity.setVisibility(View.GONE);
            holder.tvCantidadSolicitada.setText("0");
            //holder.tvNewPrice.setText(product.getNuevoPrecio());
            holder.tvNewPrice.setText(product.getPrecioRecalculado());

        } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()==null) {

            if(product.getCantidad().equals("0")) {
                holder.ivChangeQuantity.setVisibility(View.GONE);
                holder.tvCantidadSolicitada.setText("0");
                //holder.tvNewPrice.setText(product.getPrecio());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            } else {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                //holder.tvNewPrice.setText(product.getPrecio());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            }

        } else if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()==null) {

            if(tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                if(product.getCantidad().equals("0")) {
                    holder.ivChangeQuantity.setVisibility(View.GONE);
                    holder.tvCantidadSolicitada.setText("0");
                    //holder.tvNewPrice.setText(product.getPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                } else {
                    holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                    holder.tvCantidadSolicitada.setText("0");
                    //holder.tvNewPrice.setText(product.getPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                }
            } else if(tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText("0");
                //holder.tvNewPrice.setText(product.getPrecio());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            }


        } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()!=null) {

            if(tipoDocumento.equals(Constants.tipoDoc_factura)  || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                if(product.getCantidad().equals("0")) {
                    holder.ivChangeQuantity.setVisibility(View.GONE);
                    product.setCantidadSolicitada("0");
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    //holder.tvNewPrice.setText(product.getNuevoPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                } else {
                    holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    //holder.tvNewPrice.setText(product.getNuevoPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                }
            } else if(tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                //product.setCantidadSolicitada("0");
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                //holder.tvNewPrice.setText(product.getNuevoPrecio());
                holder.tvNewPrice.setText(product.getPrecioRecalculado());
            }
        }

        if(product.getEsPrecioMenorAlLimite()) {
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
                if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()!=null) {
                    holder.tvCantidadSolicitada.setText("0");
                    //holder.tvNewPrice.setText(product.getNuevoPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                    holder.tvTotalPrice.setText("0.00");

                } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()==null) {
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    //holder.tvNewPrice.setText(product.getPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                    total = Double.valueOf(product.getPrecioRecalculado()) * Integer.valueOf(product.getCantidadSolicitada());
                    holder.tvTotalPrice.setText(String.format( Constants.round_two_decimals, total));

                } else if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()==null) {
                    holder.tvCantidadSolicitada.setText("0");
                    product.setCantidadSolicitada("0");
                    //holder.tvNewPrice.setText(product.getPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                    total = Double.valueOf(product.getPrecioRecalculado());
                    holder.tvTotalPrice.setText(String.format(Constants.round_two_decimals, total));

                } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()!=null) {
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    //holder.tvNewPrice.setText(product.getNuevoPrecio());
                    holder.tvNewPrice.setText(product.getPrecioRecalculado());
                    total = Double.valueOf(product.getPrecioRecalculado()) * Integer.valueOf(product.getCantidadSolicitada());
                    holder.tvTotalPrice.setText(String.format(Constants.round_two_decimals, total));
                }

            } catch (Exception ex) {
                Log.e(Constants.log_arrow_error, ex.toString());
            }
        //} else {
        //    holder.tvTotalPrice.setText("0");
        //}

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product.setSelected(false);
                removeItem(product);
                ProductsFragment.productsAdapter.refreshItem(product, false);  // update the left list!
            }
        });

        holder.ivChangePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!product.getPrecio().isEmpty() && !product.getPre_inferior().isEmpty()) {

                    showChangePriceDialog(mContext,
                            product,
                            position,
                            product.getPrecio(),
                            product.getPre_inferior());
                } else {
                    Common.showToastMessage(mContext, "No existe precio o precio inferior para este producto");
                }
            }
        });

        holder.ivChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                    if(!product.getCantidad().isEmpty() && Integer.parseInt(product.getCantidad())>0) {
                        showChangeQuantityDialog(mContext, product, position);
                    }
                } else if(tipoDocumento.equals(Constants.tipoDoc_preventa)) {
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

    public static class QuotationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvUnidadVenta, tvDescription, tvPrice, tvQuantity, tvNewPrice, tvCantidadSolicitada, tvTotalPrice;
        ImageView ivRemove, ivArrival, ivChangePrice, ivChangeQuantity, ivVirtualStock, ivShowDialogUnidad;

        public QuotationViewHolder(View view) {
            super(view);
            tvId = (TextView)view.findViewById(R.id.tvId);
            tvUnidadVenta = (TextView)view.findViewById(R.id.tvUnidadVenta);
            tvDescription = (TextView)view.findViewById(R.id.tvDescription);
            tvNewPrice = (TextView)view.findViewById(R.id.tvNewPrice);
            tvQuantity = (TextView)view.findViewById(R.id.tvQuantity);
            tvTotalPrice = (TextView)view.findViewById(R.id.tvTotalPrice);
            tvCantidadSolicitada = (TextView)view.findViewById(R.id.tvCantidadSolicitada);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
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
        if(currPosition > -1) {
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
        product.setNuevoPrecio(product.getPrecio());
        product.setEsPrecioMenorAlLimite(false);
        product.setCantidadSolicitada("0");

        product.setNuevaUnidad(product.getUnidad());
        product.setNuevaPresentacion(product.getPresentacion());
        product.setNuevaCantidad(product.getCantidad());
        product.setPrecioRecalculado(product.getPrecio());
    }

    public void resetProducts() {
        for(int i=0; i<productsList.size(); i++) {
            resetProduct(productsList.get(i));
        }
    }

    private void showChangeQuantityDialog(final Context context, final ProductsResponse product, final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quotation_change_quantity);

        final Button btnAcceptDialog = (Button)dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button)dialog.findViewById(R.id.btnClose);
        final TextView tvProductDes = (TextView)dialog.findViewById(R.id.tvProductDes);
        final EditText edtQuantity = (EditText)dialog.findViewById(R.id.edtQuantity);
        final TextView tvStock = (TextView)dialog.findViewById(R.id.tvStock);
        final TextView tvCompareResult = (TextView)dialog.findViewById(R.id.tvCompareResult);
        final ImageView ivUpQuantity = (ImageView) dialog.findViewById(R.id.ivUpQuantity);
        final ImageView ivDownQuantity = (ImageView) dialog.findViewById(R.id.ivDownQuantity);
        //final String stock = product.getCantidad();
        final String stock = product.getNuevaCantidad();

        tvProductDes.setText(product.getNombre());
        quantityInserted = product.getCantidadSolicitada();
        tvStock.setText(stock);

        if(Integer.valueOf(quantityInserted) > 0) {
            edtQuantity.setText(quantityInserted);
        } else {
            edtQuantity.setText(quantityInserted);
            tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_mayor_a_cero));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            btnAcceptDialog.setEnabled(false);
        }

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

                if(!qInserted.isEmpty()) {

                    if(Integer.parseInt(qInserted)>0) {

                        if(tipoDocumento.equals(Constants.tipoDoc_factura) || tipoDocumento.equals(Constants.tipoDoc_proforma)) {
                            if(Integer.parseInt(qInserted) <= Integer.parseInt(stock)) {
                                tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_permitida));
                                tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                                btnAcceptDialog.setEnabled(true);
                                quantityInserted = qInserted;
                            } else {
                                tvCompareResult.setText(String.format("%s %s %s", "El máximo permitido es de", stock, "productos."));
                                tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                                btnAcceptDialog.setEnabled(false);
                                //quantityInserted = qInserted;
                            }

                        } else if(tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                            Log.d(Constants.log_arrow, "Preventa, no importa si no hay stock. Cualquier cantidad permitida");
                            tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_permitida));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                            btnAcceptDialog.setEnabled(true);
                            quantityInserted = qInserted;
                        }

                    } else if(qInserted.equals("0")){
                        tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_mayor_a_cero));
                        tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                        //tvCompareResult.setVisibility(View.VISIBLE);
                        btnAcceptDialog.setEnabled(false);
                    }

                } else {
                    tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_vacia));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    //tvCompareResult.setVisibility(View.VISIBLE);
                    btnAcceptDialog.setEnabled(false);
                }
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
                    if(newValue >= 0) {
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

    private void showChangePriceDialog(final Context context, final ProductsResponse product, final int position, final String price, final String priceMinLimit) {

        final String tipoDocumento = Singleton.getInstance().getTipoDocumento();
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quotation_change_price1);

        final Button btnAcceptDialog = (Button)dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDialog = (Button)dialog.findViewById(R.id.btnClose);
        final TextView tvPrecio = (TextView)dialog.findViewById(R.id.tvPrecio);
        final TextView tvPrecioLimiteInferior = (TextView)dialog.findViewById(R.id.tvPrecioLimInferior);
        final TextView tvPrecioIngresado = (TextView)dialog.findViewById(R.id.tvPrecioIngresado);
        final TextView tvCompareResult = (TextView)dialog.findViewById(R.id.tvCompareResult);
        final TextView tvProductDes = (TextView)dialog.findViewById(R.id.tvProductDes);
        final EditText edtPrice = (EditText)dialog.findViewById(R.id.edtPrice);
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
            tvPrecioIngresado.setText(String.format(Constants.round_three_decimals, Double.parseDouble(product.getNuevoPrecio())));
        } catch (Exception e) {
            Log.e(Constants.log_arrow_error, e.toString());
        }

        tvProductDes.setText(product.getNombre());

        precioIngresado = product.getNuevoPrecio();
        edtPrice.setText(String.valueOf(Double.parseDouble(precioIngresado)));

        String cp = Common.comparePrices(Double.valueOf(product.getNuevoPrecio()), Double.valueOf(product.getPre_inferior()));
        if(cp.equals(Constants.comparar_esMayor) || cp.equals(Constants.comparar_esIgual)) {
            tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
        } else if(cp.equals(Constants.comparar_esMenor)) {
            tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
        }

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
                //Log.v("afterTextChanged ---->", s.toString());
                String priceInserted = s.toString();

                if(!priceInserted.isEmpty() && !priceInserted.equals(".") && !priceInserted.equals(",")) {
                    tvPrecioIngresado.setText(priceInserted);
                    //el precio ingresado es... respecto al precio minimo
                    String resultComparePrices = Common.comparePrices(Double.valueOf(priceInserted), Double.valueOf(priceMinLimit));

                    if(tipoDocumento.equals(Constants.tipoDoc_factura)) {
                        if(resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                            precioIngresado = priceInserted;
                            esPrecioMenorAlLimite = false;
                            btnAcceptDialog.setEnabled(true);

                        } else if(resultComparePrices.equals(Constants.comparar_esMenor)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                            btnAcceptDialog.setEnabled(false);
                        }

                    } else if(tipoDocumento.equals(Constants.tipoDoc_proforma) || tipoDocumento.equals(Constants.tipoDoc_preventa)) {
                        if(resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                            precioIngresado = priceInserted;
                            esPrecioMenorAlLimite = false;
                            btnAcceptDialog.setEnabled(true);
                        } else if(resultComparePrices.equals(Constants.comparar_esMenor)) {
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
                if(!precioIngresado.isEmpty() && !precioIngresado.equals(".")) {
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
                if(!precioIngresado.isEmpty() && !precioIngresado.equals(".")) {
                    try {
                        Double newValue = Double.valueOf(precioIngresado) - Constants.baseDouble;
                        if(newValue >= 0) {
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
                /*product.setNuevoPrecio(precioIngresado);
                product.setEsPrecioMenorAlLimite(esPrecioMenorAlLimite);
                //refreshItem(position, product);
                refreshItems();
                dialog.dismiss();*/

                if(Common.isOnline(context)) {
                    getNewPriceRecalculatedFromChangePriceDialog(context, dialog, productsProgressBar, product, precioIngresado, btnAcceptDialog, btnCloseDialog);
                }
            }
        });

        ivShowPricesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_prices.setVisibility(View.GONE);
                ll_pricesHistory.setVisibility(View.VISIBLE);

                if(Common.isOnline(mContext)) {
                    loadPricesHistory(
                            rvPricesHistory,
                            //Singleton.getInstance().getUserCode(),
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
                if(response != null) {
                    if(response.body()!=null) {
                        ArrayList<NewPriceResponse> newPriceReca = response.body();

                        if(newPriceReca.size()>0) {
                            if(newPriceReca.get(0)!=null && !newPriceReca.get(0).getImporteRecalculado().isEmpty()) {
                                String priceRecalculated = newPriceReca.get(0).getImporteRecalculado();

                                if(!priceRecalculated.isEmpty()) {
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
                if(response != null) {
                    if(response.body()!=null) {
                        ArrayList<NewPriceResponse> newPriceReca = response.body();

                        if(newPriceReca.size()>0) {
                            if(newPriceReca.get(0)!=null && !newPriceReca.get(0).getImporteRecalculado().isEmpty()) {
                                String priceRecalculated = newPriceReca.get(0).getImporteRecalculado();

                                if(!priceRecalculated.isEmpty()) {
                                    product.setNuevaUnidad(unitSelected.getUnidad());
                                    product.setNuevaPresentacion(unitSelected.getPresentacion());
                                    product.setNuevaCantidad(unitSelected.getStock());
                                    product.setPrecioRecalculado(String.format(Constants.round_three_decimals, Double.parseDouble(priceRecalculated)));
                                    product.setNuevoPrecio(product.getPrecio());
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

    private void loadPricesHistory(final RecyclerView rv, String idCliente ,String idProduct) {

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

                    if(pricesHistory.size() > 0) {

                        PricesHistoryAdapter pricesHistoryAdapter = new PricesHistoryAdapter(mContext, pricesHistory);
                        rv.setAdapter(pricesHistoryAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rv.setLayoutManager(llm);

                    } else {
                        Log.d(Constants.log_arrow_response, "No se encontraron precios para este producto");
                        Common.showToastMessage(mContext, "No se encontraron precios para este producto");
                    }

                    //mainProgressBar.setVisibility(View.GONE);

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(mContext, "Error en el servidor");
                    //mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PricesHistoryResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_response, "response null");
                Common.showToastMessage(mContext, "Error en el servidor");
                //mainProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private void updateTotalProducts() {

        if(tvTotalProductos!=null) {
            //tvTotalProductos.setText(String.valueOf(getItemCount()));
        }
        if(tvMontoTotal!=null) {
            if(productsList.size()>0) {

                int cont = 0;
                int minQuantity = -1;
                Double suma = 0.00;
                Double precioTotalPorProducto = 0.00;
                String tipoDoc = Singleton.getInstance().getTipoDocumento();

                try {
                    for(int i=0; i<productsList.size(); i++) {

                        ProductsResponse product = productsList.get(i);

                        if(tipoDoc.equals(Constants.tipoDoc_factura)) {
                            minQuantity = 1;
                        } else if(tipoDoc.equals(Constants.tipoDoc_proforma)) {
                            minQuantity = 0;
                        }

                        if(Integer.valueOf(product.getCantidad()) >= minQuantity) {

                            if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()!=null) {
                                precioTotalPorProducto = Double.parseDouble(product.getNuevoPrecio());
                            } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()==null) {
                                Integer cantidadSolicitada = Integer.parseInt(product.getCantidadSolicitada());
                                precioTotalPorProducto = Double.parseDouble(product.getPrecio()) * cantidadSolicitada;
                            } else if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()==null) {
                                precioTotalPorProducto = Double.parseDouble(product.getPrecio());
                            } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()!=null) {
                                Integer cantidadSolicitada = Integer.parseInt(product.getCantidadSolicitada());
                                precioTotalPorProducto = Double.parseDouble(product.getNuevoPrecio()) * cantidadSolicitada;
                            }
                            //cont++;
                            suma = suma + precioTotalPorProducto;
                        }

                        if(Integer.valueOf(product.getCantidadSolicitada())>0) {
                            cont++;
                        }

                    }
                    tvTotalProductos.setText(String.valueOf(cont));
                    tvMontoTotal.setText(String.format(Constants.round_two_decimals, suma));

                    if(!Singleton.getInstance().getSaldoDisponibleCliente().isEmpty()) {
                        if(isUpToCreditLine(suma, Singleton.getInstance().getSaldoDisponibleCliente())) {
                            setSaldoDisponibleIndicator(false);
                        } else {
                            setSaldoDisponibleIndicator(true);
                        }
                    }

                } catch (Exception ex) {
                    Log.e(Constants.log_arrow_failure, ex.toString());
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
        final ImageView ivClose = (ImageView)dialog.findViewById(R.id.ivClose);
        final Button btnAcceptDialog = (Button) dialog.findViewById(R.id.btnAccept);
        progressBar = (ProgressBar)dialog.findViewById(R.id.newProgressBar);

        if(Common.isOnline(mContext)) {
            getVirtualStock(context, idUsuario, idRubro, idArticulo, rvStockVirtual);
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

    private void getVirtualStock(final Context context, String idUsuario, String idRubro, String idArticulo, final RecyclerView rvStockVirtual) {
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

                if(response != null) {
                    ArrayList<VirtualStockResponse> virtualStockList = response.body();
                    if(virtualStockList.size()>0) {
                        VirtualStockAdapter virtualStockAdapter = new VirtualStockAdapter(mContext, virtualStockList);
                        rvStockVirtual.setAdapter(virtualStockAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rvStockVirtual.setLayoutManager(llm);
                    } else {
                        Log.d(Constants.log_arrow_response, "No se encontro stock virtual para este producto");
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
                Common.showToastMessage(context, "Error en el servidor");
                Log.e(Constants.log_arrow_response, t.toString());
            }
        });

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
        final TextView tvTittle = (TextView) dialog.findViewById(R.id.tvTittle);
        final TextView tvSelectedUnit = (TextView) dialog.findViewById(R.id.tvSelectedUnit);

        tvTittle.setText(product.getId());

        //seteando unidad por defecto
        tvSelectedUnit.setText(String.format("Codigo: %s, Presentacion: %s, Stock: %s", product.getUnidad(), product.getPresentacion(), product.getCantidad()));

        if(Common.isOnline(context)) {
            getUnitsPerArticle(context, newProgressBar, product.getId(), rvUnitsPerArticle, tvSelectedUnit);
        }

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(unitsAdapter!=null && unitsAdapter.getUnitSelected()!=null) {

                    if(Common.isOnline(context)) {
                        UnitsResponse unitSelected = unitsAdapter.getUnitSelected();

                        if(!product.getNuevoPrecio().isEmpty()) {
                            getNewPriceRecalculatedFromChangeUnitDialog(unitSelected, context, dialog, productsProgressBar, product, product.getNuevoPrecio(), btnAcceptDialog, btnCloseDialog);
                        } else {
                            getNewPriceRecalculatedFromChangeUnitDialog(unitSelected, context, dialog, productsProgressBar, product, product.getPrecio(), btnAcceptDialog, btnCloseDialog);
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

                if(response != null) {
                    ArrayList<UnitsResponse> unitsPerArticleList = response.body();
                    if(unitsPerArticleList.size()>0) {

                        unitsAdapter = new UnitsAdapter(context, unitsPerArticleList, tvSelectedUnit);
                        rvUnitsPerArticle.setAdapter(unitsAdapter);
                        LinearLayoutManager llm = new LinearLayoutManager(mContext);
                        rvUnitsPerArticle.setLayoutManager(llm);

                    } else {
                        Log.d(Constants.log_arrow_response, "No se encontro stock virtual para este producto");
                        Common.showToastMessageShort(context, "No se encontro stock virtual para este producto");
                    }
                    newProgressBar.setVisibility(View.GONE);

                } else {
                    Common.showToastMessage(context, "Error en el servidor");
                    Log.e(Constants.log_arrow_response, response.message());
                    newProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UnitsResponse>> call, Throwable t) {
                newProgressBar.setVisibility(View.GONE);
                Common.showToastMessage(context, "Error en el servidor");
                Log.e(Constants.log_arrow_response, t.toString());
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
        if(saldoSuficiente) {
            tvIndicadorSaldoDisponible.setText(mContext.getString(R.string.saldo_disponible_suficiente));
            tvIndicadorSaldoDisponible.setTextColor(ContextCompat.getColor(mContext, R.color.verde));
        } else {
            tvIndicadorSaldoDisponible.setText(mContext.getString(R.string.saldo_disponible_insuficiente));
            tvIndicadorSaldoDisponible.setTextColor(ContextCompat.getColor(mContext, R.color.rojo));
        }
    }
}
