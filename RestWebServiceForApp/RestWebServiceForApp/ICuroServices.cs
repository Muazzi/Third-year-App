using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace RestWebServiceForApp
{
    
    [ServiceContract]
    public interface ICuroServices
    {

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "AddUser", BodyStyle = WebMessageBodyStyle.WrappedRequest,
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        int AddUser(string name);


        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "Create_Meeting",
            BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json,
            ResponseFormat = WebMessageFormat.Json)]

        int Create_Meeting(string P_ID, string Agenda, string Hour, string Min);
        [OperationContract]
        [WebGet(UriTemplate = "ValidateUser/{username}")]


        int ValidateUser(string username);

        [OperationContract]

        List<string> GetProjectid(string userid);

        [OperationContract]
        [WebGet(UriTemplate = "GetProjects/{userid}")]

        List<Project> GetProjects(string userid);


        [OperationContract]
        [WebGet(UriTemplate = "GetProjectInfo/{name}")]

        Project GetProjectInfo(string name);

        [OperationContract]
        [WebGet(UriTemplate = "GetProjectID/{name}")]
        Project GetProjectID(string name);

        [OperationContract]
        [WebGet(UriTemplate = "GetSprintId/{Projectname}")]

        List<string> GetSprintId(string Projectname);

        [OperationContract]

        [WebGet(UriTemplate = "GetStoryids/{ProjectName}")]
        List<String> GetStoryids(string ProjectName);

        [OperationContract]

        [WebGet(UriTemplate = "GetTasks/{ProjectName}")]

        List<UserTask> GetTasks(string ProjectName);


    }


    [DataContract]

    public class User
    {

        private int UserID;
        private string User_name;
        private string Password;
        private int Hash;
        private string DateOfBirth;
        private string EmailAdress;
        private string PhoneNumber;


        [DataMember]
        public int userid
        {
            get { return UserID; }
            set { UserID = value; }
        }

        [DataMember]
        public string name
        {
            get { return User_name; }
            set { User_name = value; }
        }

        [DataMember]
        public string password
        {
            get { return Password; }
            set { Password = value; }
        }

        [DataMember]
        public int hash
        {
            get { return Hash; }
            set { Hash = value; }
        }

        [DataMember]
        public string dateofbirth
        {
            get { return DateOfBirth; }
            set { DateOfBirth = value; }
        }

        [DataMember]
        public string email
        {
            get { return EmailAdress; }
            set { EmailAdress = value; }
        }

        [DataMember]
        public string phonenumber
        {
            get { return PhoneNumber; }
            set { PhoneNumber = value; }
        }

    }

    [DataContract]

    public class Project
    {
        private string projectID;
        private string name;

        private string description;
        private string start;
        private string end;
        private string num;
        private string cur;

        private string master;

        [DataMember]
        public string P_ID
        {
            get { return projectID; }

            set { projectID = value; }
        }

        [DataMember]
        public string ScrumMaster
        {
            get { return master; }
            set { master = value; }
        }


        [DataMember]

        public string CurrentSprint
        {
            get { return cur; }
            set { cur = value; }
        }


        [DataMember]

        public string NumSprints
        {
            get { return num; }
            set { num = value; }
        }
        [DataMember]

        public string EndDate
        {
            get { return end; }
            set { end = value; }
        }



        [DataMember]

        public string Name
        {

            get { return name; }

            set { name = value; }
        }

        [DataMember]

        public string Description
        {
            get { return description; }

            set { description = value; }
        }

        [DataMember]

        public string StartDate
        {
            get { return start; }
            set { start = value; }

        }



    }

    [DataContract]

    public class UserTask
    {
        private string def;
        private string priority;

        [DataMember]

        public string Definition
        {
            get { return def; }
              set { def = value; }
        }

        [DataMember]
        public string Priority
        {
            get { return priority; }
            set { priority = value; }

        }


    }

}
