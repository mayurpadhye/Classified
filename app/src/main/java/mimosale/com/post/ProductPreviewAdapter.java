package mimosale.com.post;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.home.shop_sale.ProductsAdapter;
import mimosale.com.home.shop_sale.ShopSaleModel;

import java.util.List;

public class ProductPreviewAdapter  extends RecyclerView.Adapter<ProductPreviewAdapter.MyViewHolder> {
    List<SalePreviewPojo> allShopList;
    Context mctx;

    public ProductPreviewAdapter(List<SalePreviewPojo> allShopList, Context mctx) {
        this.allShopList = allShopList;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_products, parent, false);
        itemView.getLayoutParams().width = (int) (getScreenWidth() / 3.5);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SalePreviewPojo items = allShopList.get(position);

        holder.tv_product_name.setText(items.getProduct_name());


    }

    @Override
    public int getItemCount() {
        return allShopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  tv_product_name;


        public MyViewHolder(View view) {
            super(view);

            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);


        }
    }
    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

}
