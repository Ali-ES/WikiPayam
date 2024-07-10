package ir.FiveMFive.FiveMFive.APIHelper;

import android.content.Context;
import android.view.View;

import java.util.List;

import ir.FiveMFive.FiveMFive.Java.Group;
import ir.FiveMFive.FiveMFive.Java.User;
import ir.FiveMFive.FiveMFive.RetrofitClient;
import ir.FiveMFive.FiveMFive.RetrofitInterface;
import ir.FiveMFive.FiveMFive.Utility.Checkers.ConnectivityChecker;
import ir.FiveMFive.FiveMFive.Utility.CredentialCrypter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupManager {
    private Context c;
    private View root;
    private List<Group> groups;
    private GroupManagerListener groupManagerListener;
    public GroupManager(Context c, View root, GroupManagerListener listener) {
        this.c = c;
        this.root = root;
        this.groupManagerListener = listener;

    }

    public interface GroupManagerListener {
        void gotGroups(List<Group> groups);
    }
    public void getGroupsList() {
        CredentialCrypter crypter = new CredentialCrypter(c);
        User user = crypter.decrypt();
        String username = user.getUsername();
        String password = user.getPassword();

        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<Group>> call = retrofitInterface.getGroupsList(username, password);
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(response.isSuccessful()) {
                    groups = response.body();
                    groupManagerListener.gotGroups(groups);
                } else {
                    ConnectivityChecker.showServerFailSnack(c, root);
                    groupManagerListener.gotGroups(null);
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                ConnectivityChecker.showConnectionFailSnack(c, root, t);
                groupManagerListener.gotGroups(null);
            }
        });
    }


}
