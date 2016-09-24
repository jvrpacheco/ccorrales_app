package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.corporacioncorrales.cotizacionesapp.activities.LoginActivity;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.PricesHistoryResponse;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.corporacioncorrales.cotizacionesapp.utils.Singleton;

import java.util.ArrayList;

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
    private TextView tvSuperaLinea;
    private String quantityInserted = "";
    private String precioIngresado = "";
    private Boolean esPrecioMenorAlLimite;

    public QuotationAdapter(Context mContext, ArrayList<ProductsResponse> productsList, TextView tvTotalProductos, TextView tvMontoTotal, TextView tvSuperaLinea) {
        this.mContext = mContext;
        this.productsList = productsList;

        //this.mActivity = (AppCompatActivity)mContext;
        this.tvTotalProductos = tvTotalProductos;
        this.tvMontoTotal = tvMontoTotal;
        this.tvSuperaLinea = tvSuperaLinea;
    }

    @Override
    public QuotationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_row_item, parent, false);
        QuotationViewHolder productsViewHolder = new QuotationViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final QuotationViewHolder holder, final int position) {
        final ProductsResponse product = productsList.get(position);
        holder.tvId.setText(product.getId());
        holder.tvDescription.setText(product.getNombre());

        //Precio
        //holder.tvPrice.setText(product.getPrecio());
        Double price = Double.parseDouble(product.getPrecio());
        holder.tvPrice.setText(String.valueOf(price));

        //Nuevo Precio y Cantidad Solicitada
        if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()!=null) {

            holder.ivChangeQuantity.setVisibility(View.GONE);
            holder.tvCantidadSolicitada.setText("0");
            holder.tvNewPrice.setText(product.getNuevoPrecio());

        } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()==null) {

            if(product.getCantidad().equals("0")) {
                holder.ivChangeQuantity.setVisibility(View.GONE);
                holder.tvCantidadSolicitada.setText("0");
                holder.tvNewPrice.setText(product.getPrecio());
            } else {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                holder.tvNewPrice.setText(product.getPrecio());
            }

        } else if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()==null) {

            if(product.getCantidad().equals("0")) {
                holder.ivChangeQuantity.setVisibility(View.GONE);
                holder.tvCantidadSolicitada.setText("0");
                holder.tvNewPrice.setText(product.getPrecio());
            } else {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText("1");
                holder.tvNewPrice.setText(product.getPrecio());
            }

        } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()!=null) {

            if(product.getCantidadSolicitada().equals("0")) {
                holder.ivChangeQuantity.setVisibility(View.GONE);
                holder.tvCantidadSolicitada.setText("0");
                holder.tvNewPrice.setText(product.getNuevoPrecio());
            } else {
                holder.ivChangeQuantity.setVisibility(View.VISIBLE);
                holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                holder.tvNewPrice.setText(product.getNuevoPrecio());
            }

        }

        if(product.getEsPrecioMenorAlLimite()) {
            holder.ivChangePrice.setImageResource(R.drawable.hand_red_52);
        } else {
            holder.ivChangePrice.setImageResource(R.drawable.hand_green_52);
        }

        //Stock
        holder.tvQuantity.setText(product.getCantidad());

        //Precio total
        if(Integer.parseInt(product.getCantidad())>0) {
            try {
                Double total = 0.00;
                if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()!=null) {
                    holder.tvCantidadSolicitada.setText("0");
                    holder.tvNewPrice.setText(product.getNuevoPrecio());
                    holder.tvTotalPrice.setText("0");

                } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()==null) {
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    holder.tvNewPrice.setText(product.getPrecio());

                    total = Double.valueOf(product.getPrecio()) * Integer.valueOf(product.getCantidadSolicitada());
                    holder.tvTotalPrice.setText(String.valueOf(total));

                } else if(product.getCantidadSolicitada()==null && product.getNuevoPrecio()==null) {

                    holder.tvCantidadSolicitada.setText("1");
                    product.setCantidadSolicitada("1");
                    holder.tvNewPrice.setText(product.getPrecio());

                    total = Double.valueOf(product.getPrecio());
                    holder.tvTotalPrice.setText(String.valueOf(total));

                } else if(product.getCantidadSolicitada()!=null && product.getNuevoPrecio()!=null) {
                    holder.tvCantidadSolicitada.setText(product.getCantidadSolicitada());
                    holder.tvNewPrice.setText(product.getNuevoPrecio());

                    total = Double.valueOf(product.getNuevoPrecio()) * Integer.valueOf(product.getCantidadSolicitada());
                    holder.tvTotalPrice.setText(String.valueOf(total));
                }
            } catch (Exception ex) {
                Log.e(Constants.log_arrow, ex.toString());
            }
        } else {
            holder.tvTotalPrice.setText("0");
        }

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

                if(!product.getCantidad().isEmpty() && Integer.parseInt(product.getCantidad())>0) {

                    showChangeQuantityDialog(mContext, product, position);
                }

            }
        });

        holder.ivArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        TextView tvId, tvDescription, tvPrice, tvQuantity, tvNewPrice, tvCantidadSolicitada, tvTotalPrice;
        ImageView ivRemove, ivArrival, ivChangePrice, ivChangeQuantity;

        public QuotationViewHolder(View view) {
            super(view);
            tvId = (TextView)view.findViewById(R.id.tvId);
            tvDescription = (TextView)view.findViewById(R.id.tvDescription);
            tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            tvNewPrice = (TextView)view.findViewById(R.id.tvNewPrice);
            tvQuantity = (TextView)view.findViewById(R.id.tvQuantity);
            tvTotalPrice = (TextView)view.findViewById(R.id.tvTotalPrice);
            tvCantidadSolicitada = (TextView)view.findViewById(R.id.tvCantidadSolicitada);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            ivArrival = (ImageView) view.findViewById(R.id.ivArrival);
            ivChangePrice = (ImageView) view.findViewById(R.id.ivChangePrice);
            ivChangeQuantity = (ImageView) view.findViewById(R.id.ivChangeQuantity);
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

    public void refreshItem(int position, ProductsResponse product) {
        //notifyItemChanged(position);
        notifyDataSetChanged();
        updateTotalProducts();
    }

    private void resetProduct(ProductsResponse product) {
        product.setNuevoPrecio(product.getPrecio());
        product.setEsPrecioMenorAlLimite(false);
        if(Integer.valueOf(product.getCantidad()) > 0) {
            product.setCantidadSolicitada("1");
        } else {
            product.setCantidadSolicitada("0");
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
        final String stock = product.getCantidad();

        tvProductDes.setText(product.getNombre());
        quantityInserted = product.getCantidadSolicitada();
        tvStock.setText(stock);
        edtQuantity.setText(quantityInserted);
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
                        if(Integer.parseInt(qInserted) <= Integer.parseInt(stock)) {
                            tvCompareResult.setText(context.getResources().getString(R.string.cantidad_solicitada_permitida));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                            btnAcceptDialog.setEnabled(true);
                            quantityInserted = qInserted;
                        } else {
                            tvCompareResult.setText(String.format("%s %s %s", "El maximo permitido es de", stock, "productos."));
                            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                            btnAcceptDialog.setEnabled(false);
                            quantityInserted = qInserted;
                        }

                    } else if(qInserted.equals("0")){
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
        });

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                product.setCantidadSolicitada(quantityInserted);
                refreshItem(position, product);
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


        //tvPrecio.setText(price);
        tvPrecio.setText(String.valueOf(Double.parseDouble(price)));

        //tvPrecioLimiteInferior.setText(priceMinLimit);
        tvPrecioLimiteInferior.setText(String.valueOf(Double.parseDouble(priceMinLimit)));

        //tvPrecioIngresado.setText(product.getNuevoPrecio());
        tvPrecioIngresado.setText(String.valueOf(Double.parseDouble(product.getNuevoPrecio())));

        tvProductDes.setText(product.getNombre());

        precioIngresado = product.getNuevoPrecio();
        //edtPrice.setText(precioIngresado);
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

                } else {
                    tvPrecioIngresado.setText("---");
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_ingresado_vacio));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    btnAcceptDialog.setEnabled(false);
                }

            }
        });

        /*ivUpPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!precioIngresado.isEmpty() && !precioIngresado.equals(".")) {
                    Double newValue = Double.valueOf(precioIngresado) + 0.1;
                    edtPrice.setText(newValue.toString());
                }
            }
        });

        ivDownPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!precioIngresado.isEmpty() && !precioIngresado.equals(".")) {
                    Double newValue = Double.valueOf(precioIngresado) - 0.1;
                    if(newValue >= 0) {
                        edtPrice.setText(newValue.toString());
                    }
                }
            }
        });*/

        btnAcceptDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                product.setNuevoPrecio(precioIngresado);
                product.setEsPrecioMenorAlLimite(esPrecioMenorAlLimite);
                refreshItem(position, product);
                dialog.dismiss();
            }
        });

        ivShowPricesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_prices.setVisibility(View.GONE);
                ll_pricesHistory.setVisibility(View.VISIBLE);

                loadPricesHistory(
                        rvPricesHistory,
                        Singleton.getInstance().getUserCode(),
                        product.getId());

                /*loadPricesHistory(
                        rvPricesHistory,
                        "197119",
                        "01VL00600");*/
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
                        //Common.showToastMessage(mContext, "No se encontraron productos para este cliente");
                    }

                    //mainProgressBar.setVisibility(View.GONE);

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    //mainProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PricesHistoryResponse>> call, Throwable t) {
                Log.d(Constants.log_arrow_response, "response null");
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
                Double suma = 0.00;
                Double precioTotalPorProducto = 0.00;

                try {
                    for(int i=0; i<productsList.size(); i++) {

                        ProductsResponse product = productsList.get(i);

                        if(Integer.valueOf(product.getCantidad())>0) {

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
                            cont++;
                            suma = suma + precioTotalPorProducto;

                        } else {


                        }
                    }
                    tvTotalProductos.setText(String.valueOf(cont));
                    tvMontoTotal.setText(String.format("%.2f",suma));

                    if(isUpToCreditLine(suma, Singleton.getInstance().getLineaDeCreditoCliente())) {
                        tvSuperaLinea.setText(Constants.superaLineaDeCredito);
                        tvSuperaLinea.setTextColor(ContextCompat.getColor(mContext, R.color.rojo));
                    } else {
                        tvSuperaLinea.setText(Constants.dentroDeLineaDeCredito);
                        tvSuperaLinea.setTextColor(ContextCompat.getColor(mContext, R.color.verde));
                    }

                } catch (Exception ex) {
                    Log.e(Constants.log_arrow_failure, ex.toString());
                }
            } else {
                tvMontoTotal.setText("0.00");
            }

        }
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

}
