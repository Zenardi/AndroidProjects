package amdeveloper.fernandesbartender;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void redirectOfficialWebSite(View view)
    {
        Uri uri = Uri.parse("http://www.fernandesbartender.com.br");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void sendMessage(View view) {

        /// Get form data
        EditText text = (EditText)findViewById(R.id.fname);
        String nome = text.getText().toString();

        text = (EditText)findViewById(R.id.lname);
        String email = text.getText().toString();

        text = (EditText)findViewById(R.id.telefone);
        String telefone = text.getText().toString();

        text = (EditText)findViewById(R.id.uname);
        String mensagem = text.getText().toString();

        text = (EditText)findViewById(R.id.dataEvento);
        String dataEvento = text.getText().toString();


        text = (EditText)findViewById(R.id.localEvento);
        String localEvento = text.getText().toString();


        if(!isValidEmail(email))
        {
            Toast.makeText(this, "E-mail Invalido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isValidDate(dataEvento, "dd/MM/yyyy"))
        {
            Toast.makeText(this, "Formato de data invalido. O padrão é: dd/mm/aaaa", Toast.LENGTH_SHORT).show();
            return;
        }


        if(nome.length() > 0 || email.length() > 0 || telefone.length() > 0 || mensagem.length() > 0 || dataEvento.length() > 0 || localEvento.length() > 0)
        {
            String emailBody = generateEmailBody(nome, email, telefone, mensagem, dataEvento, localEvento);
            /// fernandesbartender@yahoo.com.br
            /*
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","du.zenardi@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Novo Oramento - " + nome);
            intent.putExtra(Intent.EXTRA_TEXT, emailBody);
            startActivity(Intent.createChooser(intent, "Escolha um cliente de e-mail :"));
            */

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/html");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"fernandesbartender@yahoo.com.br"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Novo Orçamento - " + nome);
            i.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(emailBody));
            //i.putExtra(Intent.EXTRA_TEXT   , emailBody);
            try {
                startActivity(Intent.createChooser(i, "Escolha um cliente de e-mail"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Não há cliente de e-mail instalado.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //Show error message
            Toast.makeText(this, "Todos os campos são obrigatorios!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private boolean isValidDate(String dataEvento, String dateFormat) {
        if(dataEvento == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dataEvento);
            //System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }



    private String generateEmailBody(String nome, String email, String telefone, String mensagem, String dataEvento, String localEvento) {
        String body = "Olá Fernando, <br/> <t>Nova solicitação de orçamento de " + nome + ".<br/><br/><t>" +
                "Email do cliente: " + email + "<br/><t> Telefone de contato: " + telefone + "<br><t> Mensagem: " + mensagem + "<br><t>" +
                "Data do Evento: " + dataEvento + "<br><t>" + "Local Evento: " + localEvento;

        return body;
    }

    private  boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
