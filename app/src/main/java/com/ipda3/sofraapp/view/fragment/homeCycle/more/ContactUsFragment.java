package com.ipda3.sofraapp.view.fragment.homeCycle.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.contactUs.ContactUs;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;

public class ContactUsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.contact_us_fragment_et_name)
    EditText contactUsFragmentEtName;
    @BindView(R.id.contact_us_fragment_et_email)
    EditText contactUsFragmentEtEmail;
    @BindView(R.id.contact_us_fragment_et_phone)
    EditText contactUsFragmentEtPhone;
    @BindView(R.id.contact_us_fragment_et_message_content)
    EditText contactUsFragmentEtMessageContent;
    @BindView(R.id.contact_us_fragment_rb_complaining)
    RadioButton contactUsFragmentRbComplaining;
    @BindView(R.id.contact_us_fragment_rb_suggestion)
    RadioButton contactUsFragmentRbSuggestion;
    @BindView(R.id.contact_us_fragment_rb_enquiry)
    RadioButton contactUsFragmentRbEnquiry;

    String type;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind(this, view);

        type = "complaint";

        return view;
    }

    @OnClick({R.id.contact_us_fragment_rb_complaining, R.id.contact_us_fragment_rb_suggestion, R.id.contact_us_fragment_rb_enquiry, R.id.contact_us_fragment_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact_us_fragment_rb_complaining:
                type = "complaint";
                contactUsFragmentRbComplaining.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_on_btn));
                contactUsFragmentRbSuggestion.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_off_btn));
                contactUsFragmentRbEnquiry.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_off_btn));
                break;
            case R.id.contact_us_fragment_rb_suggestion:
                type = "suggestion";
                contactUsFragmentRbComplaining.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_off_btn));
                contactUsFragmentRbSuggestion.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_on_btn));
                contactUsFragmentRbEnquiry.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_off_btn));
                break;
            case R.id.contact_us_fragment_rb_enquiry:
                type = "inquiry";
                contactUsFragmentRbComplaining.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_off_btn));
                contactUsFragmentRbSuggestion.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_off_btn));
                contactUsFragmentRbEnquiry.setButtonDrawable(getResources().getDrawable(R.drawable.ic_radio_on_btn));
                break;
            case R.id.contact_us_fragment_btn_send:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        getClient().contactUs(
                contactUsFragmentEtName.getText().toString(),
                contactUsFragmentEtEmail.getText().toString(),
                contactUsFragmentEtPhone.getText().toString(),
                type,
                contactUsFragmentEtMessageContent.getText().toString())
                .enqueue(new Callback<ContactUs>() {
                    @Override
                    public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                        try {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ContactUs> call, Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}