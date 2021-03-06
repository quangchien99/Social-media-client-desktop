package edu.hanu.social_media_desktop_client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import edu.hanu.social_media_desktop_client.model.Comment;
import edu.hanu.social_media_desktop_client.model.Profile;
import edu.hanu.social_media_desktop_client.model.Status;
import edu.hanu.social_media_desktop_client.service.CommentService;
import edu.hanu.social_media_desktop_client.service.FriendListService;
import edu.hanu.social_media_desktop_client.service.ProfileService;
import edu.hanu.social_media_desktop_client.service.StatusService;
import edu.hanu.social_media_desktop_client.utils.PlaceHolderTextField;

public class UserStatuses {

	/**
	 * 
	 */
	private JLabel lbName;
	private JLabel lbTime;
	private JLabel lbStatus;
	private JLabel lbCommenterName;
	private JLabel lbCommentTime;
	private JLabel lbComment;
	private PlaceHolderTextField textComment;
	private JButton btnComment;

	StatusService statusService = new StatusService();
	ProfileService profileService = new ProfileService();
	FriendListService friendListService = new FriendListService();
	CommentService commentService = new CommentService();
	List<Profile> friends;
	List<Status> allStatuses = statusService.getAllStatus();

	public UserStatuses() {
		// TODO Auto-generated constructor stub
		List<Status> friendStatuses = showUserStatus(LoginGUI.userName);
		List<Comment> allComments = getAllComments();
		JPanel listContainer;
		final JFrame frame = new JFrame("Homepage");
		frame.setSize(600, 500);

		listContainer = new JPanel();
		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
		JScrollPane jScrollPane = new JScrollPane(listContainer);
		frame.add(jScrollPane, BorderLayout.CENTER);
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new GridLayout(0, 2));

		for (Status s : friendStatuses) {
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new GridLayout(0, 1));
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new FlowLayout());
			rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

			lbName = new JLabel("      " + s.getProfile().getFirstName() + " " + s.getProfile().getLastName());
			leftPanel.add(lbName);

			lbTime = new JLabel("      " + s.getCreated());
			lbTime.setFont(new Font("Serif", Font.ITALIC, 10));
			leftPanel.add(lbTime);

			lbStatus = new JLabel("      " + "      " + s.getStatus());
			lbStatus.setFont(new Font("Calibri", Font.ITALIC, 14));
			leftPanel.add(lbStatus);

			for (Comment c : allComments) {
				if (c.getStatus().getId() == s.getId()) {
					lbCommenterName = new JLabel(
							"      " + c.getProfile().getFirstName() + " " + c.getProfile().getLastName());
					leftPanel.add(lbCommenterName);

					lbCommentTime = new JLabel("      " + c.getCreated());
					lbCommentTime.setFont(new Font("Serif", Font.ITALIC, 10));
					leftPanel.add(lbCommentTime);

					lbComment = new JLabel("      " + "      " + c.getComment());
					lbComment.setFont(new Font("Calibri", Font.ITALIC, 14));
					leftPanel.add(lbComment);
				}
			}
			textComment = new PlaceHolderTextField(30);
			textComment.setPlaceholder("Add comment about this post");
			textComment.setPreferredSize(new Dimension(400, 40));
			leftPanel.add(textComment);

			btnComment = new JButton("Comment");
			btnComment.setPreferredSize(new Dimension(100, 30));
			btnComment.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(textComment.getText() + "check");
					if (textComment.getText().isEmpty()
							|| textComment.getText().equalsIgnoreCase("Add comment about this post")) {
						JOptionPane.showMessageDialog(null, "Please input your comment.");
					} else {
						Comment comment = new Comment();
						comment.setComment(textComment.getText());
						comment.setProfile(profileService.getProfile(LoginGUI.userName));
						comment.setStatus(s);
						commentService.addComment(comment);
						JOptionPane.showMessageDialog(null, "Posted");
						frame.dispose();
					}
				}
			});
			rightPanel.add(btnComment);
			leftPanel.add(rightPanel);

			JPanel cJp = new JPanel();
			cJp.setLayout(new GridLayout(1, 2));
			cJp.add(leftPanel);
			newPanel.add(cJp);
			newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));

			listContainer.add(newPanel);
			listContainer.revalidate();
		}
		frame.setVisible(true);
	}

	public List<Status> showUserStatus(String profileName) {
		List<Status> statuses = statusService.getAllStatus();
		List<Status> filteredStatuses = new ArrayList<Status>();
		for (Status status : statuses) {
			if (status.getProfile().getProfileName().equals(LoginGUI.userName)) {
				filteredStatuses.add(status);
			}
		}
		Collections.reverse(filteredStatuses);
		return filteredStatuses;
	}

	public List<Comment> getAllComments() {
		List<Comment> comments = commentService.getAllComments();
		return comments;
	}
}
