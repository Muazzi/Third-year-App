using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Runtime.Remoting.Messaging;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace RestWebServiceForApp
{
    
    public class CuroServices : ICuroServices
    {
        private string conString = ConfigurationManager.ConnectionStrings["Web_app_db"].ConnectionString;
        public int AddUser(string name)
        {
            throw new NotImplementedException();
        }

        public List<string> GetProjectid(string userid)
        {
            List<string> ProjectIdS = new List<string>();

            using (SqlConnection con = new SqlConnection(conString))
            {
                using (SqlCommand cmd = new SqlCommand("select ProjectID from [User_Project] where UserID=@user_id", con))
                {
                    cmd.Parameters.AddWithValue("@user_id", userid);
                    con.Open();
                    SqlDataReader reader = cmd.ExecuteReader();


                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            string id = reader["ProjectID"].ToString();
                            ProjectIdS.Add(id);
                        }
                    }

                }
            }

            return ProjectIdS.ToList();
        }

        public List<Project> GetProjects(string userid)
        {
            List<Project> projects = new List<Project>();
            List<string> projectids = GetProjectid(userid);

            using (SqlConnection con = new SqlConnection(conString))
            {
                con.Open();
                var sqlcmd = new SqlCommand();

                sqlcmd.Connection = con;

                sqlcmd.CommandType = CommandType.Text;

                var query = "select Name from  [Project] where ProjectID IN ({0})";
                var idParamlist = new List<string>();
                var index = 0;

                foreach (var id in projectids)
                {
                    var paramname = "@pid" + index;
                    sqlcmd.Parameters.AddWithValue(paramname, id);

                    idParamlist.Add(paramname);
                    index++;
                }

                sqlcmd.CommandText = string.Format(query, string.Join(",", idParamlist));

                SqlDataReader reader = sqlcmd.ExecuteReader();

                while (reader.Read())
                {
                    Project project = new Project();

                    project.Name = reader["Name"].ToString();

                    projects.Add(project);

                }





            }

            return projects.ToList();

        }

        public int ValidateUser(string username)
        {
            int result = 777777;

            User user = new User();
            using (SqlConnection con = new SqlConnection(conString))
            {
                using (SqlCommand cmd = new SqlCommand("select UserID from [User] where Username=@username", con))
                {
                    cmd.Parameters.AddWithValue("@username", username);
                    con.Open();

                    SqlDataReader reader = cmd.ExecuteReader();

                    if (reader.HasRows)

                    {
                        while (reader.Read())
                        {

                            result = Int32.Parse(reader["UserID"].ToString());

                        }
                    }



                }
            }

            return result;
        }


        public Project GetProjectInfo(string name)
        {

            Project myProject = new Project();

            using (SqlConnection con = new SqlConnection(conString))
            {
                using (SqlCommand cmd = new SqlCommand("select Description,StartDate,EndDate,NumSprints,CurrentSprint,ScrumMaster from [Project] where Name=@name", con))
                {
                    cmd.Parameters.AddWithValue("@name", name);
                    con.Open();

                    SqlDataReader reader = cmd.ExecuteReader();

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            myProject.CurrentSprint = reader["CurrentSprint"].ToString();
                            myProject.Description = reader["Description"].ToString();
                            myProject.StartDate = reader["StartDate"].ToString();
                            myProject.EndDate = reader["EndDate"].ToString();
                            myProject.NumSprints = reader["NumSprints"].ToString();
                            myProject.ScrumMaster = reader["ScrumMaster"].ToString();
                        }

                    }

                }

            }
            return myProject;
        }

        public Project GetProjectID(string name)
        {
           Project project = new Project();


            using (SqlConnection con = new SqlConnection(conString))
            {
                using (SqlCommand cmd = new SqlCommand("select ProjectID from [Project] where Name=@name", con))
                {
                    cmd.Parameters.AddWithValue("@name", name);
                    con.Open();

                    SqlDataReader reader = cmd.ExecuteReader();

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {

                            project.P_ID = reader["ProjectID"].ToString();

                        }

                    }

                }

            }

            return project; 

        }

        public List<string> GetSprintId(string Projectname)
        {
            List<string> Sprintids = new List<string>();


            Project project = GetProjectID(Projectname);


            using (SqlConnection con = new SqlConnection(conString))
            {
                using (SqlCommand cmd = new SqlCommand("select SprintID from [Sprint] where ProjectID=@id", con))
                {
                    cmd.Parameters.AddWithValue("@id", project.P_ID.ToString());
                    con.Open();

                    SqlDataReader reader = cmd.ExecuteReader();

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {

                            Sprintids.Add(reader["SprintID"].ToString()); 

                        }

                    }

                }

            }



            return Sprintids.ToList();
        }

        public List<string> GetStoryids(string ProjectName)
        {
            List<string> StoryIds = new List<string>();


            Project project = GetProjectID(ProjectName);

            List<string> Sprintids = GetSprintId(ProjectName);

            using (SqlConnection con = new SqlConnection(conString))
            {
                 con.Open();
                var sqlcmd = new SqlCommand();

                sqlcmd.Connection = con;

                sqlcmd.CommandType = CommandType.Text;

                var query = "select StoryID from  [Story] where SprintID IN ({0}) and  ProjectID=@p_id";
                var idParamlist = new List<string>();
                var index = 0;

                foreach (var id in Sprintids)
                {
                    var paramname = "@pid" + index;
                    sqlcmd.Parameters.AddWithValue(paramname, id);

                    idParamlist.Add(paramname);
                    index++;
                }

                sqlcmd.Parameters.AddWithValue("@p_id", project.P_ID.ToString());
                sqlcmd.CommandText = string.Format(query, string.Join(",", idParamlist));

                SqlDataReader reader = sqlcmd.ExecuteReader();

                while (reader.Read())
                {
                   

                    StoryIds.Add(reader["StoryID"].ToString());

                   

                    

                }


                return StoryIds.ToList();





            }



           
        }

        public List<UserTask> GetTasks(string ProjectName)
        {
            List<string> StoryIds = GetStoryids(ProjectName);
            List<UserTask> Tasks = new List<UserTask>();

            using (SqlConnection con = new SqlConnection(conString))
            {
                con.Open();
                var sqlcmd = new SqlCommand();

                sqlcmd.Connection = con;

                sqlcmd.CommandType = CommandType.Text;

                var query = "select Definition,PriorityLevel from  [Task] where StoryID IN ({0}) ";
                var idParamlist = new List<string>();
                var index = 0;

                foreach (var id in StoryIds)
                {
                    var paramname = "@pid" + index;
                    sqlcmd.Parameters.AddWithValue(paramname, id);

                    idParamlist.Add(paramname);
                    index++;
                }

                sqlcmd.CommandText = string.Format(query, string.Join(",", idParamlist));

                SqlDataReader reader = sqlcmd.ExecuteReader();

                while (reader.Read())
                {  
                    UserTask task= new UserTask();
                    task.Definition = reader["Definition"].ToString();
                    task.Priority = reader["PriorityLevel"].ToString();

                  Tasks.Add(task);

                }


            }
            return Tasks.ToList();
        }

        public int Create_Meeting(string P_ID, string Agenda, string Hour, string Min)
        {

            int status = 0;
            SqlConnection con = new SqlConnection(conString);

            string time = Hour + ":" + Min + ":" + "00";
            int pid= Int32.Parse(P_ID);
            con.Open(); 

            SqlCommand cmd = new SqlCommand("Insert into [Meeting] (Agenda,Time,P_id) values(@agenda,@time,@pid)",con);

            cmd.CommandType = CommandType.Text;
            cmd.Parameters.AddWithValue("@agenda", Agenda);
            cmd.Parameters.AddWithValue("@time", time);
            cmd.Parameters.AddWithValue("@pid", pid);

            cmd.ExecuteReader();
            status = 1;

            return status ;



        }
    }
}
