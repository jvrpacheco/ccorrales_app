package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;

import java.util.ArrayList;

/**
 * Created by victor on 8/20/16.
 */
public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.QuotationViewHolder> {

    Context mContext;
    ArrayList<ProductsResponse> productsList;

    public QuotationAdapter(Context mContext, ArrayList<ProductsResponse> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
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
        holder.tvPrice.setText(product.getPrecio());

        if(!product.getNuevoPrecio().isEmpty()) {
            holder.tvNewPrice.setText(product.getNuevoPrecio());
        } else {
            product.setNuevoPrecio(product.getPrecio());
            holder.tvNewPrice.setText(product.getPrecio());
        }

        if(product.getEsPrecioMenorAlLimite()) {
            holder.ivNewPrice.setImageResource(R.drawable.hand_red_52);
        } else {
            holder.ivNewPrice.setImageResource(R.drawable.hand_green_52);
        }

        holder.tvQuantity.setText(product.getCantidad());

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
                    //Common.showToastMessage(mContext, "Precio minimo:" + product.getPre_inferior());

                    showChangePriceDialog(mContext,
                            product,
                            position,
                            product.getPrecio(),
                            product.getPre_inferior());
                } else {
                    Common.showToastMessage(mContext, "No existe precio para este producto");
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

    public static class QuotationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvDescription, tvPrice, tvQuantity, tvNewPrice;
        ImageView ivRemove, ivArrival, ivChangePrice, ivNewPrice;

        public QuotationViewHolder(View view) {
            super(view);
            tvId = (TextView)view.findViewById(R.id.tvId);
            tvDescription = (TextView)view.findViewById(R.id.tvDescription);
            tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            tvNewPrice = (TextView)view.findViewById(R.id.tvNewPrice);
            tvQuantity = (TextView)view.findViewById(R.id.tvQuantity);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            ivArrival = (ImageView) view.findViewById(R.id.ivArrival);
            ivChangePrice = (ImageView) view.findViewById(R.id.ivChangePrice);
            ivNewPrice = (ImageView) view.findViewById(R.id.ivNewPrice);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeItem(ProductsResponse product) {
        int currPosition = productsList.indexOf(product);
        productsList.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    public void addItem(int position, ProductsResponse product) {
        productsList.add(position, product);
        notifyItemInserted(position);
    }

    public void refreshItem(int position, ProductsResponse product) {
        //notifyItemChanged(position);
        notifyDataSetChanged();
    }

    private String precioIngresado = "";
    private Boolean esPrecioMenorAlLimite;
    private void showChangePriceDialog(final Context context, final ProductsResponse product, final int position, final String price, final String priceMinLimit) {

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quotation_change_price);

        final Button btnAcceptDIalog = (Button)dialog.findViewById(R.id.btnAccept);
        final Button btnCloseDIalog = (Button)dialog.findViewById(R.id.btnClose);
        final TextView tvPrecio = (TextView)dialog.findViewById(R.id.tvPrecio);
        final TextView tvPrecioLimiteInferior = (TextView)dialog.findViewById(R.id.tvPrecioLimInferior);
        final TextView tvPrecioIngresado = (TextView)dialog.findViewById(R.id.tvPrecioIngresado);
        final TextView tvCompareResult = (TextView)dialog.findViewById(R.id.tvCompareResult);
        Boolean isChanged = false;

        tvPrecio.setText(price);
        tvPrecioLimiteInferior.setText(priceMinLimit);
        tvPrecioIngresado.setText(product.getNuevoPrecio());

        String cp = Common.comparePrices(Double.valueOf(product.getNuevoPrecio()), Double.valueOf(product.getPre_inferior()));

        if(cp.equals(Constants.comparar_esMayor) || cp.equals(Constants.comparar_esIgual)) {
            tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
        } else if(cp.equals(Constants.comparar_esMenor)) {
            tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
            tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
        }

        EditText edtPrice = (EditText)dialog.findViewById(R.id.edtPrice);
        edtPrice.setText(product.getNuevoPrecio());
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
                Log.v("afterTextChanged ---->", s.toString());
                String priceInserted = s.toString();

                if(!priceInserted.isEmpty() && !priceInserted.equals(".")) {
                    tvPrecioIngresado.setText(priceInserted);
                    //el precio ingresado es... respecto al precio minimo
                    String resultComparePrices = Common.comparePrices(Double.valueOf(priceInserted), Double.valueOf(priceMinLimit));

                    if(resultComparePrices.equals(Constants.comparar_esMayor) || resultComparePrices.equals(Constants.comparar_esIgual)) {
                        tvCompareResult.setText(context.getResources().getString(R.string.precio_dentro_del_rango));
                        tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.verde));
                        precioIngresado = priceInserted;
                        esPrecioMenorAlLimite = false;
                        btnAcceptDIalog.setEnabled(true);
                    } else if(resultComparePrices.equals(Constants.comparar_esMenor)) {
                        tvCompareResult.setText(context.getResources().getString(R.string.precio_fuera_del_rango));
                        tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                        precioIngresado = priceInserted;
                        esPrecioMenorAlLimite = true;
                        btnAcceptDIalog.setEnabled(true);
                    }

                } else {
                    tvPrecioIngresado.setText("---");
                    tvCompareResult.setText(context.getResources().getString(R.string.precio_ingresado_vacio));
                    tvCompareResult.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    btnAcceptDIalog.setEnabled(false);
                }

            }
        });

        btnAcceptDIalog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                product.setNuevoPrecio(precioIngresado);
                product.setEsPrecioMenorAlLimite(esPrecioMenorAlLimite);
                refreshItem(position, product);
                dialog.dismiss();
            }
        });

        btnCloseDIalog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
