## INTRODUCTION

The OpenNebula EC2 Query is a web service that enables you to launch and manage virtual machines 
in your OpenNebula installation through the  Amazon EC2 Query Interface. In this way, you can use
any EC2 Query tool or utility to access your Private Cloud. The EC2 Query web service is implemented
 upon the new OpenNebula Cloud API (OCA) layer that exposes the full capabilities of an OpenNebula 
 private cloud; and  Sinatra, a widely used light web framework. 

The current implementation includes the basic routines to use a Cloud, namely: image upload and 
registration, and the VM run, describe and terminate operations. The following sections explains
 you how to install and configure the EC2 Query web service on top of a running OpenNebula cloud. 
 
 
## CLOUD LOADING CONFIGURATION FILE FORMAT

[cloudelement]
Name = 
Interface = 
type = 
endpoint = 
accesskey = 
secretkey = 
instancetype = 
image = 
max = 
version = 
signatureversion = 
signaturemethod = 
