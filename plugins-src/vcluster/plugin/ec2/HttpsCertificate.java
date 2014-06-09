package vcluster.plugin.ec2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpsCertificate
{
    public static final String TRUST_STORE          = "javax.net.ssl.trustStore";
    public static final String TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    public static final String KEY_STORE            = "javax.net.ssl.keyStore";
    public static final String KEY_STORE_PASSWORD   = "javax.net.ssl.keyStorePassword";

    private String _clientCertStoreFile = null;
    private InputStream _clientCertInputStream = null;
    private String _clientCertStorePswd = null;
    private String _serverCertStoreFile = null;
    private InputStream _serverCertInputStream = null;
    private String _serverCertStorePswd = null;

    private KeyManagerFactory _kmFactory;
    private TrustManagerFactory _tmFactory;

    private static boolean _debug = false;

    /**
     * If we just care server side cert, not client side cert.
     * @param serverCertStoreFile String
     * @param serverCertStorePswd String
     */
    public HttpsCertificate(String serverCertStoreFile, String serverCertStorePswd)
    {
        _serverCertStoreFile = serverCertStoreFile;
        _serverCertStorePswd = serverCertStorePswd;
    }

    public HttpsCertificate(InputStream serverCertInputStream, String serverCertStorePswd)
    {
        _serverCertInputStream = serverCertInputStream;
        _serverCertStorePswd = serverCertStorePswd;
    }

    /**
     * If we care both server side and client side certs.
     * @param clientCertStoreFile String
     * @param clientCertStorePswd String
     * @param serverCertStoreFile String
     * @param serverCertStorePswd String
     */
    public HttpsCertificate(String clientCertStoreFile, String clientCertStorePswd,
                            String serverCertStoreFile, String serverCertStorePswd)
    {
        _clientCertStoreFile = clientCertStoreFile;
        _clientCertStorePswd = clientCertStorePswd;
        _serverCertStoreFile = serverCertStoreFile;
        _serverCertStorePswd = serverCertStorePswd;
    }

    public HttpsCertificate(InputStream clientCertInputStream, String clientCertStorePswd,
                            InputStream serverCertInputStream, String serverCertStorePswd)
    {
        _clientCertInputStream = clientCertInputStream;
        _clientCertStorePswd = clientCertStorePswd;
        _serverCertInputStream = serverCertInputStream;
        _serverCertStorePswd = serverCertStorePswd;
    }

    /**
     * This version works for all stand-alone apps, not for web apps.
     */
    public void setCertForStandAlone()
    {
        if (_clientCertStoreFile != null)
        {
            System.setProperty(TRUST_STORE, _clientCertStoreFile);
            System.setProperty(TRUST_STORE_PASSWORD, _clientCertStorePswd);
        }
        if (_serverCertStoreFile != null)
        {
            System.setProperty(KEY_STORE, _serverCertStoreFile);
            System.setProperty(KEY_STORE_PASSWORD, _serverCertStorePswd);
        }
    }

    /**
     * This version works for web apps.
     */
    public void setCertForWebApps(Object httpConn)
    {
        setClientCert();
        setServerCert();

        try
        {
            // now create the context
            SSLContext ctx = SSLContext.getInstance("SSL");

            if (_kmFactory != null)
            {
                if (_tmFactory != null)
                {
                    ctx.init(_kmFactory.getKeyManagers(), _tmFactory.getTrustManagers(), null);
                }
                else
                {
                    ctx.init(_kmFactory.getKeyManagers(), null, null);
                }
            }
            else
            {
                if (_tmFactory != null)
                {
                    ctx.init(null, _tmFactory.getTrustManagers(), null);
                }
                else
                {
                    return; // if both null, there is no need to set anything at all.
                }
            }

            // without the following line, we would get the following error:
            // in a web server, hit it the first time, we get this error:
            //     javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
            // but hit it again, it's fine. Looks like it didn't take what we set for the default.
            // so we set it in this method.
            // Furthermore, for backward compatibility, we have to check these,
            // thanks to IBM and SUN for making this world more miserable!
            System.out.println("httpConn Class=" + httpConn.getClass().getName());
           
        }
        catch (NoSuchAlgorithmException nsae)
        {
            nsae.printStackTrace();
           // throw new HttpException("error in loading SSL context");
        }
        catch (KeyManagementException kme)
        {
            kme.printStackTrace();
           // throw new HttpException("error on initializing SSL context");
        }
    }

    private void setClientCert()
    {
        debug("set client side cert ...");
        if (_clientCertStoreFile != null)
        {
            debug("read client side file ...");
            _clientCertInputStream = getKeystoreInputStream(_clientCertStoreFile);
        }

        try
        {
            if (_clientCertInputStream != null)
            {
                debug("setting client side cert ...");
                KeyStore clientCertKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                clientCertKeyStore.load(_clientCertInputStream, _clientCertStorePswd.toCharArray());
                _kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                _kmFactory.init(clientCertKeyStore, _clientCertStorePswd.toCharArray());
            }
            else
            {
                debug("nothing to set for the client cert!");
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
           // throw new HttpException("can't load client certificate file");
        }
        catch (KeyStoreException kse)
        {
            kse.printStackTrace();
           // throw new HttpException("can't find keystore instance");
        }
        catch (NoSuchAlgorithmException nsae)
        {
            nsae.printStackTrace();
            //throw new HttpException("error in loading client cert");
        }
        catch (CertificateException ce)
        {
            ce.printStackTrace();
           // throw new HttpException("error in reading");
        }
        catch (UnrecoverableKeyException uke)
        {
            uke.printStackTrace();
           // throw new HttpException("error in initializing client cert");
        }
    }

    private void setServerCert()
    {
        debug("set server side cert ...");
        if (_serverCertStoreFile != null)
        {
            debug("read server side cert ...");
            _serverCertInputStream = getKeystoreInputStream(_serverCertStoreFile);
        }
        if (_serverCertInputStream != null)
        {
            try
            {
                debug("setting server side cert ...");
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(_serverCertInputStream, _serverCertStorePswd.toCharArray());
                _tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                _tmFactory.init(trustStore);
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
              //  throw new HttpException("can't load server certificate file");
            }
            catch (KeyStoreException kse)
            {
                kse.printStackTrace();
             //   throw new HttpException("can't find server cert keystore instance");
            }
            catch (NoSuchAlgorithmException nsae)
            {
                nsae.printStackTrace();
              //  throw new HttpException("error in loading server cert");
            }
            catch (CertificateException ce)
            {
                ce.printStackTrace();
               // throw new HttpException("error in reading server cert");
            }
        }
        else
        {
            debug("Nothing to set for the server cert");
        }
    }

    private InputStream getKeystoreInputStream(String fileName)
    {
        InputStream retInputStream = null;

        if (fileName == null) return retInputStream;

        try
        {
            retInputStream = new FileInputStream(fileName);
            return retInputStream;
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println("info: failed to get file: " + fileName + " through filesystem: " + fnfe.getMessage());
            System.out.println("Note: this is just info, not necessarily a fault! Will try with classpath loading.");
            retInputStream = this.getClass().getResourceAsStream(fileName);
        }

        //retInputStream = HTTPSMessage.class.getResourceAsStream(fileName);
        //retInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        debug("The input stream is: " + retInputStream);
        return retInputStream;
    }

    private static void debug(String s)
    {
        if (_debug == true && s != null) System.out.println("HttpsCertificate: - " + s);
    }
}

